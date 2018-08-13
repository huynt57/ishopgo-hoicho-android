package ishopgo.com.exhibition.ui.main.product.icheckproduct

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.base.BaseIcheckSingleObserver

class IcheckProductViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var detail = MutableLiveData<IcheckProductDetail>()

    fun loadDetail(request: String) {
        addDisposable(isgService.getIcheckProductDetail(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckProductDetail>() {
                    override fun success(data: IcheckProductDetail?) {
                        data?.let { detail.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataSalePoint = MutableLiveData<IcheckSalePointManager>()

    fun loadSalePoint(request: String) {
        addDisposable(isgService.getIcheckSalePoint(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckSalePointManager>() {
                    override fun success(data: IcheckSalePointManager?) {
                        data?.let { dataSalePoint.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataSalePointDetail = MutableLiveData<IcheckSalePointDetail>()

    fun getSalePointDetail(request: String) {
        addDisposable(isgService.getIcheckDetailSalePoint(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckSalePointDetailManager>() {
                    override fun success(data: IcheckSalePointDetailManager?) {
                        data?.let { dataSalePointDetail.postValue(it.local) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataProductSalePoint = MutableLiveData<List<IcheckProduct>>()

    fun loadProductSalePointDetail(request: String) {
        addDisposable(isgService.getIcheckProductSalePoint(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckProductManager>() {
                    override fun success(data: IcheckProductManager?) {
                        data?.let { dataProductSalePoint.postValue(it.list) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataProductRelated = MutableLiveData<List<IcheckProduct>>()

    fun loadProductRelated(request: String) {
        addDisposable(isgService.getIcheckProductRelated(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckProduct>>() {
                    override fun success(data: List<IcheckProduct>?) {
                        data?.let { dataProductRelated.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataReview = MutableLiveData<List<IcheckReview>>()

    fun loadIcheckReview(request: String) {
        addDisposable(isgService.getIcheckReview(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckReview>>() {
                    override fun success(data: List<IcheckReview>?) {
                        data?.let { dataReview.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataShopInfo = MutableLiveData<IcheckShopDetail>()

    fun loadIcheckShopInfo(request: String) {
        addDisposable(isgService.getIcheckShopInfo(request)
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckShopDetail>() {
                    override fun success(data: IcheckShopDetail?) {
                        data?.let { dataShopInfo.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataShopCategory = MutableLiveData<List<IcheckCategory>>()

    fun loadIcheckShopCategory(request: String) {
        addDisposable(isgService.loadIcheckShopCategory(request)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckCategoryManager>() {
                    override fun success(data: IcheckCategoryManager?) {
                        data?.let { dataShopCategory.postValue(it.category?.roots) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataShopCategoryProduct = MutableLiveData<List<IcheckProduct>>()

    fun loadIcheckShopCategoryProduct(request: String) {
        addDisposable(isgService.loadIcheckShopCategoryProduct(request)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckProduct>>() {
                    override fun success(data: List<IcheckProduct>?) {
                        data?.let { dataShopCategoryProduct.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }
}