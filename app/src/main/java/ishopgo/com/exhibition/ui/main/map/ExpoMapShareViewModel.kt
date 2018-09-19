package ishopgo.com.exhibition.ui.main.map

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class ExpoMapShareViewModel : BaseApiViewModel() {

    var showQrCodeFragment = MutableLiveData<ExpoConfig>()

    fun openQrCode(data: ExpoConfig) {
        showQrCodeFragment.postValue(data)
    }

    var showTicketDetailFragment = MutableLiveData<Ticket>()

    fun openTicketDetail(data: Ticket) {
        showTicketDetailFragment.postValue(data)
    }

    var showBoothSelectFragment = MutableLiveData<Long>()

    fun openBoothSelected(id: Long) {
        showBoothSelectFragment.postValue(id)
    }

    var showRegisterExpoFragment = MutableLiveData<Boolean>()

    fun openRegisterExpo() {
        showRegisterExpoFragment.postValue(true)
    }

    var showSearchBoothFragment = MutableLiveData<Long>()

    fun openSearchBoothFragment(id: Long) {
        showSearchBoothFragment.postValue(id)
    }

    var addBoothSuccess = MutableLiveData<Boolean>()

    fun addBoothSuccess() {
        addBoothSuccess.postValue(true)
    }
}