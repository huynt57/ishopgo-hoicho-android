package ishopgo.com.exhibition.ui.main.home.search.shop

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchShopRequest
import ishopgo.com.exhibition.domain.response.ManagerShop
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class SearchShopsViewModel : BaseListViewModel<List<SearchShopResultProvider>>(), AppComponent.Injectable {
    var total = MutableLiveData<Int>()

    override fun loadData(params: Request) {
        if (params is SearchShopRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["q"] = params.keyword

            addDisposable(noAuthService.searchShops(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManagerShop>() {
                        override fun success(data: ManagerShop?) {
                            total.postValue(data?.total ?: 0)
                            dataReturned.postValue(data?.booth ?: mutableListOf())
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