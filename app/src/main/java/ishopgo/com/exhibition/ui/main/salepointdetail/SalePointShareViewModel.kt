package ishopgo.com.exhibition.ui.main.salepointdetail

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class SalePointShareViewModel : BaseApiViewModel() {

    var showSalePointQRCode = MutableLiveData<SearchSalePoint>()

    fun showSalePointQRCode(data: SearchSalePoint) {
        showSalePointQRCode.postValue(data)
    }
}