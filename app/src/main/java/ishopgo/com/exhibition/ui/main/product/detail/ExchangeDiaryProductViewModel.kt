package ishopgo.com.exhibition.ui.main.product.detail

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.domain.response.ListBGBN
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class ExchangeDiaryProductViewModel : BaseApiViewModel() {

    var isExchangeSusscess = MutableLiveData<Boolean>()

    fun createExchangeDiarySusscess() {
        isExchangeSusscess.postValue(true)
    }

    var resultdBenGiao = MutableLiveData<ListBGBN>()

    fun selectedBenGiao(data: ListBGBN) {
        resultdBenGiao.postValue(data)
    }

    var resultdBenNhan = MutableLiveData<ListBGBN>()

    fun selectedBenNhan(data: ListBGBN) {
        resultdBenNhan.postValue(data)
    }
}