package ishopgo.com.exhibition.ui.main.map

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.ExpoShopLocationRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.ExpoShop
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 6/11/18. HappyCoding!
 */
class ExpoShopViewModel : BaseListViewModel<List<ExpoShop>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is ExpoShopLocationRequest) {
            addDisposable(noAuthService.getExpoShopLocations(params.expoId, params.toMap())
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<ExpoShop>>() {
                        override fun success(data: List<ExpoShop>?) {
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