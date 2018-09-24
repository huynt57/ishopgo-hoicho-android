package ishopgo.com.exhibition.ui.main.stamp.buystamp.update

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.domain.response.StampListBuy
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class ShareStampOrderViewModel : BaseApiViewModel() {
    var showStampOrderHistory = MutableLiveData<StampListBuy>()

    fun openSearchProduct(data: StampListBuy) {
        showStampOrderHistory.postValue(data)
    }
}