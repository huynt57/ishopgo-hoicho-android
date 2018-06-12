package ishopgo.com.exhibition.ui.community.notification

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class CommunityNotificationActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return CommunityNotificationFragmentActionBar()
    }
}