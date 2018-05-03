package ishopgo.com.exhibition.ui.main.home.search.shop

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.RequestParams
import ishopgo.com.exhibition.domain.request.SearchShopRequestParams
import ishopgo.com.exhibition.domain.response.Shop
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class SearchShopsViewModel : BaseListViewModel<List<SearchShopResultProvider>>(), AppComponent.Injectable {

    override fun loadData(params: RequestParams) {
        if (params is SearchShopRequestParams) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["q"] = params.keyword

            addDisposable(noAuthService.searchShops(fields)
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : BaseSingleObserver<List<Shop>>() {
                        override fun success(data: List<Shop>?) {
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