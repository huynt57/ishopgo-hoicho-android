package ishopgo.com.exhibition.ui.main.home.search.product

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchProductRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class SearchProductViewModel : BaseListViewModel<List<SearchProductProvider>>(), AppComponent.Injectable {

    companion object {
        private val TAG = "SearchProductViewModel"
    }

    override fun loadData(params: Request) {
        if (params is SearchProductRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["q"] = params.keyword

            addDisposable(noAuthService.searchProducts(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                        override fun success(data: List<Product>?) {
                            dataReturned.postValue(data ?: mutableListOf<Product>())
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