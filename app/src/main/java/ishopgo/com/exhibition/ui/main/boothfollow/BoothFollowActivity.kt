package ishopgo.com.exhibition.ui.main.boothfollow

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class BoothFollowActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return BoothFollowFragmentActionBar.newInstance(startupOption)
    }
}