package ishopgo.com.exhibition.ui.chat.local.conversation

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.db.MessageRepository
import ishopgo.com.exhibition.domain.response.ConversationInfo
import ishopgo.com.exhibition.domain.response.LocalMessageItem
import ishopgo.com.exhibition.domain.response.TextPattern
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.chat.IChatMessage
import ishopgo.com.exhibition.model.chat.PreparedMessage
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by xuanhong on 4/11/18. HappyCoding!
 */
class ConversationViewModel : BaseApiViewModel(), AppComponent.Injectable {

    companion object {
        private val TAG = "ConversationViewModel"
    }

    @Inject
    lateinit var application: Application

    @Inject
    lateinit var messageRepo: MessageRepository

    var messages = MutableLiveData<List<IChatMessage>>()
    var patternUpdated = MutableLiveData<Boolean>()
    var patternCreated = MutableLiveData<TextPattern>()
    var patternRemoved = MutableLiveData<Boolean>()
    var conversationInfo = MutableLiveData<ConversationInfo>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun getConversationInfo(conversationId: String) {
        val fields = mutableMapOf<String, Any>()
        fields.put("conver", conversationId)

        addDisposable(isgService.chat_conversationInfo(fields)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<ConversationInfo>() {
                    override fun success(data: ConversationInfo?) {
                        data?.let { conversationInfo.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                }))
    }


    fun getMessages(conversationId: String, lastId: Long = -1L) {
        val fields = mutableMapOf<String, Any>()
        fields.put("id_conversion", conversationId)
        fields.put("limit", Const.PAGE_LIMIT)
        if (lastId != -1L) fields.put("last_id", lastId)

        addDisposable(isgService.inbox_getMessages(fields)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<List<LocalMessageItem>>() {
                    override fun success(data: List<LocalMessageItem>?) {
                        messages.postValue(data ?: listOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                }))
    }

    fun sendTextMessage(idConversions: String, text: String) {
        val tempId = generateTempId()

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("id", idConversions)
        builder.addFormDataPart("ui_id", tempId)
        builder.addFormDataPart("message", text)

        saveSendingMessage(idConversions, UserDataManager.currentUserId, tempId, text, null, null)

        addDisposable(isgService.chat_sendChat(builder.build())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        Log.d(TAG, "send message ok:  $data")
                    }

                    override fun failure(status: Int, message: String) {
                        Log.d(TAG, "send message failed: $message")
                        markMessageFailed(tempId)
                    }

                }))
    }

    fun sendImageMessage(idConversions: String, imageUri: Uri) {
        val tempId = generateTempId()

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("id", idConversions)
        builder.addFormDataPart("ui_id", tempId)

        val imageFile = File(application.cacheDir, "postImage_${SystemClock.currentThreadTimeMillis()}}.jpg")
        imageFile.deleteOnExit()
        Toolbox.reEncodeBitmap(application, imageUri, 640, Uri.fromFile(imageFile))
        val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
        builder.addFormDataPart("image", imageFile.name, imageBody)

        saveSendingMessage(idConversions, UserDataManager.currentUserId, tempId, null, null, null)

        addDisposable(isgService.chat_sendChat(builder.build())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        Log.d(TAG, "send message ok:  $data")
                    }

                    override fun failure(status: Int, message: String) {
                        Log.d(TAG, "send message failed: $message")
                        markMessageFailed(tempId)
                    }

                }))
    }

    fun sendImagesFromInventory(idConversions: String, uris: MutableList<Uri>) {
        val tempId = generateTempId()

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("id", idConversions)
        builder.addFormDataPart("ui_id", tempId)
        uris.map { builder.addFormDataPart("listImages[]", it.toString()) }

        saveSendingMessage(idConversions, UserDataManager.currentUserId, tempId, null, null, null)

        addDisposable(isgService.chat_sendChat(builder.build())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        Log.d(TAG, "send message ok:  $data")
                    }

                    override fun failure(status: Int, message: String) {
                        Log.d(TAG, "send message failed: $message")
                        markMessageFailed(tempId)
                    }

                }))
    }

    fun removePattern(idConversions: String, patternId: Long) {
        addDisposable(isgService.chat_removeSampleMessage(patternId)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        patternRemoved.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )
    }

    fun updatePattern(idConversions: String, updatedPattern: TextPattern) {
        val fields = mutableMapOf<String, Any>()
        fields.put("id", updatedPattern.id)
        fields.put("content", updatedPattern.content ?: "")

        addDisposable(isgService.chat_updateSampleMessages(fields)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<TextPattern>() {
                    override fun success(data: TextPattern?) {
                        patternUpdated.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )
    }

    fun addPattern(idConversions: String, content: String) {
        val fields = mutableMapOf<String, Any>()
        fields.put("content", content)

        addDisposable(isgService.chat_updateSampleMessages(fields)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<TextPattern>() {
                    override fun success(data: TextPattern?) {
                        val created = TextPattern()
                        created.content = content
                        patternCreated.postValue(created)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )

    }

    private fun generateTempId(): String {
        return "${System.currentTimeMillis()}"
    }

    private fun saveSendingMessage(idConversions: String, from: Long, tempId: String, text: String? = null, image: Uri? = null, listImages: List<Uri>? = null) {
        val sendingMessage = PreparedMessage()
        sendingMessage.idConversation = idConversions
        sendingMessage.from = from
        sendingMessage.text = text ?: ""
        sendingMessage.status = IChatMessage.STATUS_SENDING
        sendingMessage.idTemp = tempId
        image?.let { sendingMessage.imageUris = image.path }
        listImages?.let { sendingMessage.imageUris = listImages.joinToString() }

        addDisposable(messageRepo.add(sendingMessage)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        // ignored
                    }

                    override fun onError(e: Throwable?) {
                        Log.d(TAG, "save sending message failed: $e")
                    }

                })
        )
    }

    fun markMessageFailed(tempId: String) {
        if (tempId.isNotBlank()) {
            // mark temp message in db is failed
            addDisposable(messageRepo.setMessageFailed(tempId)
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : DisposableCompletableObserver() {
                        override fun onComplete() {
                            // ignored
                        }

                        override fun onError(e: Throwable?) {
                            e?.printStackTrace()
                        }

                    }))

//            failedMessageId.postValue(tempId)
        }
    }

}