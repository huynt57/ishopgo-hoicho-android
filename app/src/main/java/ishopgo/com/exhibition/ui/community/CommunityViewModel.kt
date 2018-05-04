package ishopgo.com.exhibition.ui.community

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.util.Log
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreCommunityRequest
import ishopgo.com.exhibition.model.Community.Community
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.model.Community.CommunityComment
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.widget.Toolbox
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by hoangnh on 4/23/2018.
 */
class CommunityViewModel : BaseListViewModel<List<CommunityProvider>>(), AppComponent.Injectable {
    var sentShareSuccess = MutableLiveData<Boolean>()

    @SuppressLint("StaticFieldLeak")
    @Inject
    lateinit var appContext: Application

    override fun loadData(params: Request) {
        if (params is LoadMoreCommunityRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["last_id"] = params.last_id

            addDisposable(noAuthService.getCommunity(fields)
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : BaseSingleObserver<List<Community>>() {
                        override fun success(data: List<Community>?) {
                            dataReturned.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }


                    })
            )
        }
    }

    fun sentShareCommunity(share: String, postMedias: ArrayList<PostMedia> = ArrayList()) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", share)

        if (postMedias.isNotEmpty()) {
            for (i in postMedias.indices) {
                val uri = postMedias[i].uri
                Log.d("listImage[]", uri.toString())

                val imageFile = File(appContext.cacheDir, "postImage$i.jpg")
                imageFile.deleteOnExit()
                Toolbox.reEncodeBitmap(appContext, uri, 2048, Uri.fromFile(imageFile))
                val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
                builder.addFormDataPart("images[]", imageFile.name, imageBody)
            }
        }

        addDisposable(authService.sentPostCommunity(builder.build())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        sentShareSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var postCommentSusscess = MutableLiveData<Boolean>()

    fun postCommentCommunity(post_id: Long, content: String, postMedias: ArrayList<PostMedia> = ArrayList()) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", content)

        if (postMedias.isNotEmpty()) {
            for (i in postMedias.indices) {
                val uri = postMedias[i].uri
                Log.d("listImage[]", uri.toString())

                val imageFile = File(appContext.cacheDir, "postImage$i.jpg")
                imageFile.deleteOnExit()
                Toolbox.reEncodeBitmap(appContext, uri, 2048, Uri.fromFile(imageFile))
                val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
                builder.addFormDataPart("images[]", imageFile.name, imageBody)
            }
        }

        addDisposable(authService.postCommentCommunity(post_id, builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        postCommentSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var postLikeSuccess = MutableLiveData<Any>()

    fun postCommunityLike(post_id: Long) {
        addDisposable(authService.postCommunityLike(post_id)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        postLikeSuccess.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var loadCommentSusscess = MutableLiveData<MutableList<CommunityComment>>()

    fun loadCommentCommunity(post_id: Long, parent_id: Long, params: Request) {
        if (params is LoadMoreCommunityRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["last_id"] = params.last_id
            fields["parent_id"] = parent_id
            addDisposable(authService.getCommentCommunity(post_id, fields)
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : BaseSingleObserver<MutableList<CommunityComment>>() {
                        override fun success(data: MutableList<CommunityComment>?) {
                            loadCommentSusscess.postValue(data)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }
}