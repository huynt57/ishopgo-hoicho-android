package ishopgo.com.exhibition.ui.main.shop.relate

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.ShopRelateRequest
import ishopgo.com.exhibition.domain.response.BoothRelate
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class RelateShopViewModel : BaseListViewModel<List<BoothRelate>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is ShopRelateRequest) {
            val fields = mutableMapOf<String, Any>()
//            fields["limit"] = params.limit
//            fields["offset"] = params.offset

            addDisposable(noAuthService.getShopRelate(params.shopId, fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<BoothRelate>>() {
                        override fun success(data: List<BoothRelate>?) {
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