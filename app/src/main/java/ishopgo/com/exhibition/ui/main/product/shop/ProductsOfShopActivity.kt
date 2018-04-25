package ishopgo.com.exhibition.ui.main.product.shop

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class ProductsOfShopActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return ProductsOfShopFragmentActionBar.newInstance(startupOption)
    }


}