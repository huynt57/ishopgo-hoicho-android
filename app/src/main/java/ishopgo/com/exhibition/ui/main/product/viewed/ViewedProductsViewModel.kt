package ishopgo.com.exhibition.ui.main.product.viewed

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.ViewedProductsRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.main.product.ProductProvider

class ViewedProductsViewModel : BaseListViewModel<List<ProductProvider>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is ViewedProductsRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            if (params.productId != -1L)
                fields["product_id"] = params.productId

            addDisposable(noAuthService.getViewedProducts(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                        override fun success(data: List<Product>?) {
                            dataReturned.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }


                    })
            )
        }
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}
