package ishopgo.com.exhibition.ui.chat.local.info

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.ConversationInfo
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/16/18. HappyCoding!
 */
class MemberInfoViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var infoUpdated = MutableLiveData<ConversationInfo>()
    var info = MutableLiveData<ConversationInfo>()

    fun changeName(conversationId: String, title: CharSequence?, image: Nothing?) {
        val fields = mutableMapOf<String, Any>()
        fields.put("conver", conversationId)
        title?.let {
            fields.put("title", title)
        }
        addDisposable(isgService.chat_updateInfoGroup(fields)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<ConversationInfo>() {
                    override fun success(data: ConversationInfo?) {
                        data?.let {
                            infoUpdated.postValue(it)
                        }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )
    }

    fun getConversationInfo(conversationId: String) {
        val fields = mutableMapOf<String, Any>()
        fields.put("conver", conversationId)

        addDisposable(isgService.chat_conversationInfo(fields)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<ConversationInfo>() {
                    override fun success(data: ConversationInfo?) {
                        data?.let { info.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                }))
    }

}