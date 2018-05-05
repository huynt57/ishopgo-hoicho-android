package ishopgo.com.exhibition.ui.main.product.detail.comment

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.util.Log
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.ProductCommentsRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.ProductComment
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.widget.Toolbox
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ProductCommentViewModel : BaseListViewModel<List<ProductCommentProvider>>(), AppComponent.Injectable {
    @SuppressLint("StaticFieldLeak")
    @Inject
    lateinit var appContext: Application

    override fun loadData(params: Request) {
        if (params is ProductCommentsRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            if (params.lastId != -1L) fields["last_id"] = params.lastId
            if (params.parentId != -1L) fields["parent_id"] = params.parentId

            addDisposable(noAuthService.getProductComments(params.productId, fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<ProductComment>>() {
                        override fun success(data: List<ProductComment>?) {
                            dataReturned.postValue(data ?: listOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }


                    })
            )
        }
    }

    var postCommentSuccess = MutableLiveData<Boolean>()

    fun postCommentProduct(productId: Long, content: String, parentId: Long, postMedias: ArrayList<PostMedia> = ArrayList()) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", content)
                .addFormDataPart("parent_id", parentId.toString())

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
        addDisposable(authService.postCommentProduct(productId, builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        postCommentSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}