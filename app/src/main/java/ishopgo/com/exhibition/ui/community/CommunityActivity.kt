package ishopgo.com.exhibition.ui.community

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseActivity
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailFragment

/**
 * Created by hoangnh on 4/23/2018.
 */
class CommunityActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return CommunityFragment.newInstance(startupOption)
    }
}