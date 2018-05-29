package ishopgo.com.exhibition.ui.main.ticket

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class TicketViewModel : BaseApiViewModel(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var crateSusscess = MutableLiveData<Boolean>()

    fun createTicket() {
        addDisposable(authService.createTicket()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        crateSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var getTicketData = MutableLiveData<Ticket>()

    fun getTicket() {
        addDisposable(authService.getTicket()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Ticket>() {
                    override fun success(data: Ticket?) {
                        getTicketData.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                    }
                }))
    }
}