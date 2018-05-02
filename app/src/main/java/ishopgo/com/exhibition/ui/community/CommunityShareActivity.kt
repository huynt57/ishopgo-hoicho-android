package ishopgo.com.exhibition.ui.community

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by hoangnh on 4/26/2018.
 */
class CommunityShareActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return CommunityShareFragmentActionBar()
    }
}