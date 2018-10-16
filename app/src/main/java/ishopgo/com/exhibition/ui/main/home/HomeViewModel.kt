package ishopgo.com.exhibition.ui.main.home

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.ExposRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.postmenu.PostMenuManager
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/26/18. HappyCoding!
 */
class HomeViewModel : BaseApiViewModel(), AppComponent.Injectable {

    companion object {
        const val DEFAULT_MAX_HOME_PRODUCT_ITEMS = 10
        const val TYPE_CURRENT = 0
        const val TYPE_GOING = 1
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var highlightBrand = MutableLiveData<List<Brand>>()

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
        params["limit"] = 5
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

    var scuentificCouncils = MutableLiveData<MutableList<ScuentificCouncils.Advisor>>()

    fun loadScuentificCouncils() {
        addDisposable(noAuthService.getScuentificCouncils()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<ScuentificCouncils>() {
                    override fun success(data: ScuentificCouncils?) {
                        data?.let {
                            scuentificCouncils.postValue(it.advisors
                                    ?: mutableListOf())
                        }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )

    }

    var highlightProducts = MutableLiveData<List<Product>>()

    fun loadHighlightProducts() {
        val params = mutableMapOf<String, Any>()
        params["limit"] = 20
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

    var viewedProducts = MutableLiveData<List<Product>>()

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

    var suggestedProducts = MutableLiveData<List<Product>>()

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

    var favoriteProducts = MutableLiveData<List<Product>>()

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

    var newestProducts = MutableLiveData<List<Product>>()

    fun loadNewestProducts() {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = DEFAULT_MAX_HOME_PRODUCT_ITEMS
        fields["offset"] = 0

        addDisposable(noAuthService.getNewestProducts(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<NewestProducts>() {
                    override fun success(data: NewestProducts?) {
                        newestProducts.postValue(data?.data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )
    }

    var promotionProducts = MutableLiveData<List<Product>>()

    fun loadPromotionProducts() {
        val fields = mutableMapOf<String, Any>()
        fields["limit"] = DEFAULT_MAX_HOME_PRODUCT_ITEMS
        fields["offset"] = 0

        addDisposable(noAuthService.getPromotionProducts(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                    override fun success(data: List<Product>?) {
                        promotionProducts.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )
    }

    var categories = MutableLiveData<List<Category>>()

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

    var footer = MutableLiveData<String>()

    fun loadFooter() {
        addDisposable(noAuthService.getConfigFooter()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<String>() {
                    override fun success(data: String?) {
                        footer.postValue(data ?: "")
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )
    }

    var exposFair = MutableLiveData<List<ExpoConfig>>()
    var exposFairGoing = MutableLiveData<Boolean>()
    var exposFairCurrent = MutableLiveData<Boolean>()

    fun loadExpoFair(params: Request) {
        if (params is ExposRequest) {
            val requestGoing = ExposRequest()
            requestGoing.limit = Const.PAGE_LIMIT
            requestGoing.offset = 0
            requestGoing.time = TYPE_GOING
            val goingEvent = noAuthService.getExpos(requestGoing.toMap())

            val requestCurrent = ExposRequest()
            requestCurrent.limit = Const.PAGE_LIMIT
            requestCurrent.offset = 0
            requestCurrent.time = TYPE_CURRENT
            val currentEvent = noAuthService.getExpos(requestCurrent.toMap())

            addDisposable(
                    goingEvent.flatMap {
                        if (it.data?.isEmpty() == true) {
                            return@flatMap currentEvent
                        } else {
                            return@flatMap Single.just(it)
                        }
                    }
                            .subscribeOn(Schedulers.single())
                            .subscribeWith(object : BaseSingleObserver<List<ExpoConfig>>() {
                                override fun success(data: List<ExpoConfig>?) {
                                    exposFair.postValue(data ?: listOf())
                                }

                                override fun failure(status: Int, message: String) {
                                    resolveError(status, message)
                                }
                            })
            )
        }
    }
}