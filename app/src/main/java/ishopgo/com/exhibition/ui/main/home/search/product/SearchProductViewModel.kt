package ishopgo.com.exhibition.ui.main.home.search.product

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchProductRequest
import ishopgo.com.exhibition.domain.response.ManagerProduct
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class SearchProductViewModel : BaseListViewModel<List<SearchProductProvider>>(), AppComponent.Injectable {

    companion object {
        private val TAG = "SearchProductViewModel"
    }

    var total = MutableLiveData<Int>()

    override fun loadData(params: Request) {
        if (params is SearchProductRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["q"] = params.keyword

            addDisposable(noAuthService.searchProducts(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManagerProduct>() {
                        override fun success(data: ManagerProduct?) {
                            total.postValue(data?.total ?: 0)
                            dataReturned.postValue(data?.product ?: mutableListOf<Product>())
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