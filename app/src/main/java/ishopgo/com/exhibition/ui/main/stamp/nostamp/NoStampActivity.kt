package ishopgo.com.exhibition.ui.main.stamp.nostamp

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class NoStampActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return NoStampFragmentActionBar.newInstance(startupOption)
    }
}