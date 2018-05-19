package ishopgo.com.exhibition.ui.chat.local.config.notification

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/7/18. HappyCoding!
 */
class NotificationConfigActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return NotificationConfigFragment()
    }


}