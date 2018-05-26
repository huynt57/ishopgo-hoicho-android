package ishopgo.com.exhibition.ui.main.home.search.sale_point

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchSalePointRequest
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.main.salepoint.SalePointProvider

class SearchSalePointViewModel : BaseListViewModel<List<SalePointProvider>>(), AppComponent.Injectable {

    companion object {
        private val TAG = "SearchProductViewModel"
    }

    override fun loadData(params: Request) {
        if (params is SearchSalePointRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["q"] = params.keyword

//            addDisposable(noAuthService.searchProducts(fields)
//                    .subscribeOn(Schedulers.single())
//                    .subscribeWith(object : BaseSingleObserver<List<Product>>() {
//                        override fun success(data: List<Product>?) {
//                            dataReturned.postValue(data ?: mutableListOf<Product>())
//                        }
//
//                        override fun failure(status: Int, message: String) {
//                            resolveError(status, message)
//                        }
//
//
//                    })
//            )
        }
    }

    override fun inject(appComponent: AppComponent) {
//        appComponent.inject(this)
    }

}