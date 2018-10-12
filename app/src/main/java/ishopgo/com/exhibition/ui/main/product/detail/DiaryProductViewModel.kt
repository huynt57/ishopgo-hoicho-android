package ishopgo.com.exhibition.ui.main.product.detail

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class DiaryProductViewModel : BaseApiViewModel() {

    var isSusscess = MutableLiveData<Boolean>()

    fun createDiarySusscess() {
        isSusscess.postValue(true)
    }

    var showDiaryTabFragment = MutableLiveData<Boolean>()

    fun openDiaryTabFragment(status: Boolean) {
        showDiaryTabFragment.postValue(status)
    }
}