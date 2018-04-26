package ishopgo.com.exhibition.ui.main

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider
import ishopgo.com.exhibition.ui.main.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.product.ProductProvider

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MainViewModel : BaseApiViewModel(), AppComponent.Injectable {

    var isSearchEnable = MutableLiveData<Boolean>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun enableSearch() {
        isSearchEnable.postValue(true)
    }

    fun disableSearch() {
        isSearchEnable.postValue(false)
    }

}