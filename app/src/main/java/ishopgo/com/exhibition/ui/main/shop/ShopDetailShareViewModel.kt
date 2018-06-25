package ishopgo.com.exhibition.ui.main.shop

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class ShopDetailShareViewModel : BaseApiViewModel() {

    var showCategoriedProducts = MutableLiveData<Category>()

    fun showCategoriedProducts(category: Category) {
        showCategoriedProducts.postValue(category)
    }

}