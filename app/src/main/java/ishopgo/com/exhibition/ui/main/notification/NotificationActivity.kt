package ishopgo.com.exhibition.ui.main.notification

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by hoangnh on 5/7/2018.
 */
class NotificationActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return NotificationFragmentActionBar()
    }
}