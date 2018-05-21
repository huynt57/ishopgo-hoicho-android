package ishopgo.com.exhibition.ui.main.product.detail

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.domain.response.ProductComment
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.ProductLike
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentProvider
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class ProductDetailViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var sameShopProducts = MutableLiveData<List<ProductProvider>>()

    fun loadSameShopProducts(productId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = 10
        fields["offset"] = 0
        fields["product_id"] = productId
        addDisposable(noAuthService.getRelateProducts(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                    override fun success(data: List<Product>?) {
                        sameShopProducts.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var viewedProducts = MutableLiveData<List<ProductProvider>>()

    fun loadViewedProducts(productId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = 10
        fields["offset"] = 0
        fields["product_id"] = productId

        addDisposable(noAuthService.getViewedProducts(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                    override fun success(data: List<Product>?) {
                        viewedProducts.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )
    }

    var favoriteProducts = MutableLiveData<List<ProductProvider>>()

    fun loadFavoriteProducts(productId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = 10
        fields["offset"] = 0
        fields["product_id"] = productId

        addDisposable(authService.getFavoriteProducts(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                    override fun success(data: List<Product>?) {
                        favoriteProducts.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )
    }

    var detail = MutableLiveData<ProductDetailProvider>()

    fun loadProductDetail(productId: Long) {
        addDisposable(noAuthService.getProductDetail(productId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<ProductDetail>() {
                    override fun success(data: ProductDetail?) {
                        data?.let {
                            detail.postValue(it)
                        }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )
    }

    var productComments = MutableLiveData<List<ProductCommentProvider>>()

    fun loadProductComments(productId: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = 5
        addDisposable(noAuthService.getProductComments(productId, fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<ProductComment>>() {
                    override fun success(data: List<ProductComment>?) {
                        productComments.postValue(data ?: listOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )
    }

    var getProductLike = MutableLiveData<ProductLike>()

    fun getProductLike(productId: Long) {
        addDisposable(authService.getProductLike(productId)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<ProductLike>() {
                    override fun success(data: ProductLike?) {
                        getProductLike.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    fun postShareProduct(productId: Long) {
        addDisposable(authService.postProductShare(productId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {

                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var postLikeSuccess = MutableLiveData<Any>()

    fun postProductLike(productId: Long) {
        addDisposable(authService.postProductLike(productId)
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

    @SuppressLint("StaticFieldLeak")
    @Inject
    lateinit var appContext: Application

    var postCommentSuccess = MutableLiveData<Boolean>()

    fun postCommentProduct(productId: Long, content: String, parentId: Long, postMedias: ArrayList<PostMedia> = ArrayList()) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", content)
                .addFormDataPart("parent_id", parentId.toString())

        if (postMedias.isNotEmpty()) {
            for (i in postMedias.indices) {
                val uri = postMedias[i].uri
                uri?.let {
                    val imageFile = File(appContext.cacheDir, "postImage$i.jpg")
                    imageFile.deleteOnExit()
                    Toolbox.reEncodeBitmap(appContext, it, 2048, Uri.fromFile(imageFile))
                    val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
                    builder.addFormDataPart("images[]", imageFile.name, imageBody)
                }
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
}