package ishopgo.com.exhibition.ui.main.home.search.sale_point

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchSalePointRequest
import ishopgo.com.exhibition.model.search_sale_point.ManagerSearchSalePoint
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class SearchSalePointViewModel : BaseListViewModel<List<SearchSalePoint>>(), AppComponent.Injectable {
    var total = MutableLiveData<Int>()

    override fun loadData(params: Request) {
        if (params is SearchSalePointRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["q"] = params.keyword

            addDisposable(noAuthService.searchSalePoint(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManagerSearchSalePoint>() {
                        override fun success(data: ManagerSearchSalePoint?) {
                            total.postValue(data?.total ?: 0)
                            dataReturned.postValue(data?.salePoint ?: mutableListOf())
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