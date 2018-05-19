package ishopgo.com.exhibition.ui.main.product.detail.sale_point

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.ProductSalePointRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.model.ProductSalePoint
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.main.product.detail.ProductSalePointProvider

class ProductSalePointViewModel : BaseListViewModel<List<ProductSalePointProvider>>(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun loadData(params: Request) {
        if (params is ProductSalePointRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(authService.getProductSalePoint(params.productId, fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<ProductSalePoint>>() {
                        override fun success(data: List<ProductSalePoint>?) {
                            dataReturned.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }
}