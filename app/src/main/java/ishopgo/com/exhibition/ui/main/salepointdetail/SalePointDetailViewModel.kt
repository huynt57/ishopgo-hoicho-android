package ishopgo.com.exhibition.ui.main.salepointdetail

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.search_sale_point.ManagerSalePointDetail
import ishopgo.com.exhibition.model.search_sale_point.ManagerSearchSalePoint
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class SalePointDetailViewModel : BaseApiViewModel(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var getData = MutableLiveData<ManagerSalePointDetail>()

    fun loadData(accountId: Long) {

        addDisposable(noAuthService.getSalePointDetail(accountId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<ManagerSalePointDetail>() {
                    override fun success(data: ManagerSalePointDetail?) {
                        getData.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }
}