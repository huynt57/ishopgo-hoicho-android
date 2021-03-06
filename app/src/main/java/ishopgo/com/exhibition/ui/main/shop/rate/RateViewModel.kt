package ishopgo.com.exhibition.ui.main.shop.rate

import android.arch.lifecycle.MutableLiveData
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
class RateViewModel : BaseListViewModel<List<ShopRate>>(), AppComponent.Injectable {

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

    var createRateSusscess = MutableLiveData<Boolean>()

    fun createProductSalePoint(shopId: Long, content: String, rate_point: Int) {
        val fields = mutableMapOf<String, Any>()
        fields["content"] = content
        fields["rate_point"] = rate_point


        addDisposable(authService.createRatingShop(shopId, fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        createRateSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }
}