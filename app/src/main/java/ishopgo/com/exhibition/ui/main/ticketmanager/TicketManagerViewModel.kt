package ishopgo.com.exhibition.ui.main.ticketmanager

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.TicketRequest
import ishopgo.com.exhibition.model.ManagerTicket
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class TicketManagerViewModel : BaseListViewModel<List<Ticket>>(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var total = MutableLiveData<Int>()

    override fun loadData(params: Request) {
        if (params is TicketRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            if (params.payment_status != 0L)
                fields["payment_status"] = params.payment_status


            addDisposable(authService.getListTicket(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManagerTicket>() {
                        override fun success(data: ManagerTicket?) {
                            total.postValue(data?.total ?: 0)
                            dataReturned.postValue(data?.ticket ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }
}