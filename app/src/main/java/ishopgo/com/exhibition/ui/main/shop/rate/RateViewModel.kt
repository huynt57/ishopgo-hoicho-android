package ishopgo.com.exhibition.ui.main.shop.rate

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.ShopRatesRequest
import ishopgo.com.exhibition.domain.response.ShopRate
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class RateViewModel : BaseListViewModel<List<ShopRateProvider>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is ShopRatesRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(noAuthService.getShopRatings(params.shopId, fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<ShopRate>>() {
                        override fun success(data: List<ShopRate>?) {
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