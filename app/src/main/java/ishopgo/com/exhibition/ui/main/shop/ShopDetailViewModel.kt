package ishopgo.com.exhibition.ui.main.shop

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ShopDetailViewModel : BaseApiViewModel(), AppComponent.Injectable {

    var shopImage = MutableLiveData<String>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun updateShopImage(url: String) {
        shopImage.postValue(url)
    }

}