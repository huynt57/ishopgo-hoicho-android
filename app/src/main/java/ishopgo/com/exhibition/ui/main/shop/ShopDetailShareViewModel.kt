package ishopgo.com.exhibition.ui.main.shop

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class ShopDetailShareViewModel : BaseApiViewModel() {

    var showCategoriedProducts = MutableLiveData<CategoryProvider>()

    fun showCategoriedProducts(category: CategoryProvider) {
        showCategoriedProducts.postValue(category)
    }

}