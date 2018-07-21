package ishopgo.com.exhibition.ui.main.shop.info

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.ShopRelateRequest
import ishopgo.com.exhibition.domain.response.BoothRelate
import ishopgo.com.exhibition.domain.response.ManagerShopDetail
import ishopgo.com.exhibition.domain.response.ShopDetail
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ShopInfoViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var info = MutableLiveData<ShopDetail>()
    var listSalePoint = MutableLiveData<List<SearchSalePoint>>()
    var shopRelates = MutableLiveData<List<BoothRelate>>()

    fun loadInfo(shopId: Long) {
        val request = if (UserDataManager.currentUserId > 0) authService.getShopInfo(shopId) else noAuthService.getShopInfo(shopId)
        addDisposable(request
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<ManagerShopDetail>() {
                    override fun success(data: ManagerShopDetail?) {
                        info.postValue(data?.booth)
                        listSalePoint.postValue(data?.salePoint)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )
    }

    fun loadShopRelates(params: Request) {
        if (params is ShopRelateRequest) {
            val fields = mutableMapOf<String, Any>()
//            fields["limit"] = params.limit
//            fields["offset"] = params.offset

            addDisposable(noAuthService.getShopRelate(params.shopId, fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<BoothRelate>>() {
                        override fun success(data: List<BoothRelate>?) {
                            shopRelates.postValue(data ?: listOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }


                    })
            )
        }
    }

}