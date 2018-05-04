package ishopgo.com.exhibition.ui.community

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.util.Log
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreLastIdRequestParams
import ishopgo.com.exhibition.model.Community.Community
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.domain.request.Request
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
        if (params is LoadMoreLastIdRequestParams) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["last_id"] = params.last_id

            addDisposable(authService.getCommunity(fields)
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

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }
}