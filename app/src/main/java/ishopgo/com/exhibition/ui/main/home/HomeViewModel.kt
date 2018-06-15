package ishopgo.com.exhibition.ui.main.home

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.Banner
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.postmenu.PostMenuManager
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.product.ProductProvider

/**
 * Created by xuanhong on 4/26/18. HappyCoding!
 */
class HomeViewModel : BaseApiViewModel(), AppComponent.Injectable {

    companion object {
        const val DEFAULT_MAX_HOME_ITEMS = 5
        const val DEFAULT_MAX_HOME_PRODUCT_ITEMS = 10
    }

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

    var latestNews = MutableLiveData<PostMenuManager>()

    fun loadLatestNews() {
        val params = mutableMapOf<String, Any>()
        params["limit"] = DEFAULT_MAX_HOME_ITEMS
        params["offset"] = 0
        params["type"] = 1 // news

        addDisposable(noAuthService.getPost(params)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<PostMenuManager>() {
                    override fun success(data: PostMenuManager?) {
                        data?.let {
                            latestNews.postValue(it)
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
        params["limit"] = DEFAULT_MAX_HOME_ITEMS
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
        fields["limit"] = DEFAULT_MAX_HOME_PRODUCT_ITEMS
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
        fields["limit"] = DEFAULT_MAX_HOME_PRODUCT_ITEMS
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
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = DEFAULT_MAX_HOME_PRODUCT_ITEMS
        fields["offset"] = 0

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

    var categories = MutableLiveData<List<CategoryProvider>>()

    fun loadCategories() {
        addDisposable(noAuthService.getCategories()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Category>>() {
                    override fun success(data: List<Category>?) {
                        val filtered = data?.filter { it.id != 0L }
                        filtered?.map { it.level = 1 }
                        categories.postValue(filtered ?: mutableListOf())
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