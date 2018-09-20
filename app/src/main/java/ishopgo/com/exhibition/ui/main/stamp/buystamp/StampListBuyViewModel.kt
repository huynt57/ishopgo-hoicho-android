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
import okhttp3.MultipartBody

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

    fun getStampOrderStatistical() {
        addDisposable(authService.getStampOrderStatistical()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<StampOrdersStatistical>() {
                    override fun success(data: StampOrdersStatistical?) {
                        statistical.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var updateBuyStampSuccess = MutableLiveData<StampListBuy>()

    fun updateBuyStamp(id: Long, status: Long, saleNote: String, price: Long, quantity: String) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("status", status.toString())
                .addFormDataPart("sale_note", saleNote)
                .addFormDataPart("price", price.toString())
                .addFormDataPart("quantity", quantity)

        addDisposable(authService.updateStampOrder(id, builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<StampListBuy>() {
                    override fun success(data: StampListBuy?) {
                        updateBuyStampSuccess.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }
}