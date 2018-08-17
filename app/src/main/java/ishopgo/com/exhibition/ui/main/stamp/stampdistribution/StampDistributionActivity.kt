package ishopgo.com.exhibition.ui.main.stamp.stampdistribution

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class StampDistributionActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return StampDistributionFragmentActionBar.newInstance(startupOption)
    }
}