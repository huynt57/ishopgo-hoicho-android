package ishopgo.com.exhibition.ui.main.configbooth

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.domain.response.BoothRelate
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class ShareBoothViewModel : BaseApiViewModel() {
    var showBoothRelateFloat = MutableLiveData<Boolean>()

    fun openBoothRelateFragment() {
        showBoothRelateFloat.postValue(true)
    }

    var getDataBoothRelate = MutableLiveData<BoothRelate>()

    fun getDataBoothRelated(data: BoothRelate) {
        getDataBoothRelate.postValue(data)
    }
}