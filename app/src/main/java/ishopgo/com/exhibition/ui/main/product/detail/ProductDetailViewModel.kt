package ishopgo.com.exhibition.ui.main.product.detail

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.domain.response.ProductComment
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.ProductLike
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentProvider

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
                .subscribeOn(Schedulers.io())
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
        val dummy = mutableListOf<ProductProvider>()
        for (i in 0..5)
            dummy.add(object : IdentityData(), ProductProvider {

                init {
                    id = i.toLong()
                }

                override fun provideImage(): String {
                    return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
                }

                override fun provideName(): String {
                    return "Kem trị thâm mụn Medi Spotless (Đã xem)"
                }

                override fun providePrice(): String {
                    return "520.000 đ"
                }

                override fun provideMarketPrice(): String {
                    return "510.000 đ"
                }

            })

        viewedProducts.postValue(dummy)
    }

    var favoriteProducts = MutableLiveData<List<ProductProvider>>()

    fun loadFavoriteProducts(productId: Long) {
        val dummy = mutableListOf<ProductProvider>()
        for (i in 0..5)
            dummy.add(object : IdentityData(), ProductProvider {

                init {
                    id = i.toLong()
                }

                override fun provideImage(): String {
                    return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
                }

                override fun provideName(): String {
                    return "Kem trị thâm mụn Medi Spotlessý (Yêu thích)"
                }

                override fun providePrice(): String {
                    return "520.000 đ"
                }

                override fun provideMarketPrice(): String {
                    return "510.000 đ"
                }

            })

        favoriteProducts.postValue(dummy)
    }

    var detail = MutableLiveData<ProductDetailProvider>()

    fun loadProductDetail(productId: Long) {
        addDisposable(noAuthService.getProductDetail(productId)
                .subscribeOn(Schedulers.io())
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
                .subscribeOn(Schedulers.io())
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
}