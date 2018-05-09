package ishopgo.com.exhibition.ui.main.product.suggested

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class SuggestedProductsActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return SuggestedProductsFragmentActionBar.newInstance(startupOption)
    }


}