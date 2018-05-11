package ishopgo.com.exhibition.ui.main.shop

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ShopDetailActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return ShopDetailFragmentActionBar.newInstance(startupOption)
    }

    override fun onBackPressed() {
        if (currentFragment is BackpressConsumable && (currentFragment as BackpressConsumable).onBackPressConsumed())
            return

        super.onBackPressed()
    }

    override fun startupOptions(): Bundle {
        return intent?.extras ?: Bundle()
    }

}