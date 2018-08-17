package ishopgo.com.exhibition.ui.main.stamp.stampwarning

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class StampWarningActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return StampWarningFragmentActionBar.newInstance(startupOption)
    }
}