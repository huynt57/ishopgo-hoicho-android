package ishopgo.com.exhibition.ui.main.stamp.buystamp

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.StampDistribution
import ishopgo.com.exhibition.domain.response.StampListBuy
import ishopgo.com.exhibition.domain.response.StampManager
import ishopgo.com.exhibition.domain.response.StampOrdersStatistical
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class StampListBuyViewModel : BaseListViewModel<List<StampListBuy>>(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(authService.getStampOrders(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<StampListBuy>>() {
                        override fun success(data: List<StampListBuy>?) {
                            dataReturned.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var statistical = MutableLiveData<StampOrdersStatistical>()

    fun getStampOrderStatistical(){
        addDisposable(authService.getStampOrderStatistical()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<StampOrdersStatistical>() {
                    override fun success(data:StampOrdersStatistical?) {
                        statistical.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }
}