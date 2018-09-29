package ishopgo.com.exhibition.ui.main.product.detail

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class ExchangeDiaryProductViewModel : BaseApiViewModel() {

    var isExchangeSusscess = MutableLiveData<Boolean>()

    fun createExchangeDiarySusscess() {
        isExchangeSusscess.postValue(true)
    }
}