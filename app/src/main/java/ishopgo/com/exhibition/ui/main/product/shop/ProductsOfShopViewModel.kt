package ishopgo.com.exhibition.ui.main.product.shop

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SameShopProductsRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.main.product.ProductProvider

class ProductsOfShopViewModel : BaseListViewModel<List<ProductProvider>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is SameShopProductsRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["product_id"] = params.productId
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(noAuthService.getRelateProducts(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                        override fun success(data: List<Product>?) {
                            dataReturned.postValue(data ?: listOf())
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
