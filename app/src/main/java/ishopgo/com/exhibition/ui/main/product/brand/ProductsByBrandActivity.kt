package ishopgo.com.exhibition.ui.main.product.brand

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class ProductsByBrandActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return ProductsByBrandFragment.newInstance(startupOption)
    }


}