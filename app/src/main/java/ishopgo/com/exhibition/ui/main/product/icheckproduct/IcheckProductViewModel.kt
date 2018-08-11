package ishopgo.com.exhibition.ui.main.product.icheckproduct

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.IcheckProductDetail
import ishopgo.com.exhibition.domain.response.IcheckSalePointManager
import ishopgo.com.exhibition.domain.response.Product
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

    var featuredProducts = MutableLiveData<List<Product>>()

    fun loadFeaturedProducts(request: Request) {
        if (request is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()

            fields["limit"] = request.limit
            fields["offset"] = request.offset

            addDisposable(noAuthService.getHighlightProducts(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                        override fun success(data: List<Product>?) {
                            featuredProducts.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
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
}