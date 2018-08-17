package ishopgo.com.exhibition.ui.main.stamp.stampmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class StampManagerActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return StampManagerFragmentActionBar.newInstance(startupOption)
    }
}