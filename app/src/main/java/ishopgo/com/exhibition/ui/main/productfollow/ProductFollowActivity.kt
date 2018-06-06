package ishopgo.com.exhibition.ui.main.productfollow

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class ProductFollowActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return ProductFollowFragmentActionBar.newInstance(startupOption)
    }
}