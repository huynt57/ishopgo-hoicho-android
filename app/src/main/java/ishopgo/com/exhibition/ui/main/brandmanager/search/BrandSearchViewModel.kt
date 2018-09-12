package ishopgo.com.exhibition.ui.main.brandmanager.search

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class BrandSearchViewModel : BaseApiViewModel(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var getDataSearchBrands = MutableLiveData<Brand>()

    fun getDataSearchBrands(data: Brand) {
        getDataSearchBrands.value = data
    }
}