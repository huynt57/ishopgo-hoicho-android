package ishopgo.com.exhibition.ui.main.product.branded

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.BrandProductsRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.SearchProducts
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class ProductsOfBrandViewModel : BaseListViewModel<SearchProducts>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is BrandProductsRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            params.name?.let {
                fields["name"] = it
            }

            addDisposable(noAuthService.getBrandProducts(params.brandId, fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<SearchProducts>() {
                        override fun success(data: SearchProducts?) {
                            data?.let {
                                dataReturned.postValue(it)
                            }
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
