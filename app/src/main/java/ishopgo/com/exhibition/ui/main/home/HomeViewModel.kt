package ishopgo.com.exhibition.ui.main.home

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.product.ProductProvider

/**
 * Created by xuanhong on 4/26/18. HappyCoding!
 */
class HomeViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var highlightBrand = MutableLiveData<List<HighlightBrandProvider>>()

    fun loadHighlightBrands() {
        addDisposable(noAuthService.getHighlightBrands()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Brand>>() {
                    override fun success(data: List<Brand>?) {
                        data?.let {
                            highlightBrand.postValue(it)
                        }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )

    }

    var highlightProducts = MutableLiveData<List<ProductProvider>>()

    fun loadHighlightProducts() {
        val params = mutableMapOf<String, Any>()
        params["limit"] = 5
        params["offset"] = 0

        addDisposable(noAuthService.getHighlightProducts(params)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                    override fun success(data: List<Product>?) {
                        data?.let {
                            highlightProducts.postValue(it)
                        }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )

    }

    var viewedProducts = MutableLiveData<List<ProductProvider>>()

    fun loadViewedProducts() {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = 10
        fields["offset"] = 0

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

    var suggestedProducts = MutableLiveData<List<ProductProvider>>()

    fun loadSuggestedProducts() {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = 10
        fields["offset"] = 0

        addDisposable(noAuthService.getSuggestedProducts(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                    override fun success(data: List<Product>?) {
                        suggestedProducts.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )
    }

    var favoriteProducts = MutableLiveData<List<ProductProvider>>()

    fun loadFavoriteProducts() {
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

    var categories = MutableLiveData<List<CategoryProvider>>()

    fun loadCategories() {
        addDisposable(noAuthService.getCategories()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Category>>() {
                    override fun success(data: List<Category>?) {
                        data?.map { it.level = 1 }
                        categories.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )
    }

    var banners = MutableLiveData<List<Banner>>()

    fun loadBanners() {
        addDisposable(noAuthService.getBanners()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Banner>>() {
                    override fun success(data: List<Banner>?) {
                        banners.postValue(data ?: listOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )
    }

}