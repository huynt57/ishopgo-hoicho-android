package ishopgo.com.exhibition.ui.main.ticket

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class TicketViewModel : BaseListViewModel<List<Ticket>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        addDisposable(authService.getMyTicket()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Ticket>>() {
                    override fun success(data: List<Ticket>?) {
                        dataReturned.postValue(data ?: listOf())
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