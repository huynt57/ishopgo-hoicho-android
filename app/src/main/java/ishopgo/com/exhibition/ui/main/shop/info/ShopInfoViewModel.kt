package ishopgo.com.exhibition.ui.main.shop.info

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.ShopDetail
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ShopInfoViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var info = MutableLiveData<ShopInfoProvider>()

    fun loadInfo(shopId: Long) {
        addDisposable(noAuthService.getShopInfo(shopId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<ShopDetail>() {
                    override fun success(data: ShopDetail?) {
                        info.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )
    }

}