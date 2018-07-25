package ishopgo.com.exhibition.ui.filterproduct

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.model.FilterProduct
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class FilterProductViewModel : BaseApiViewModel(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var showFragmentFilter = MutableLiveData<FilterProduct>()

    fun showFragmentFilter(filterProduct: FilterProduct) {
        showFragmentFilter.value = filterProduct
    }

    var getDataFilter = MutableLiveData<FilterProduct>()

    fun getDataFilter(data: FilterProduct) {
        getDataFilter.value = data
    }
}