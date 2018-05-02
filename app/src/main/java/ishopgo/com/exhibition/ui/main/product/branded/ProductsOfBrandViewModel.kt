package ishopgo.com.exhibition.ui.main.product.branded

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.BrandProductsRequest
import ishopgo.com.exhibition.domain.request.RequestParams
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.main.product.ProductProvider

class ProductsOfBrandViewModel : BaseListViewModel<List<ProductProvider>>(), AppComponent.Injectable {

    override fun loadData(params: RequestParams) {
        if (params is BrandProductsRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(noAuthService.getBrandProducts(params.brandId, fields)
                    .subscribeOn(Schedulers.io())
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
