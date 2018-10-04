package ishopgo.com.exhibition.ui.main.scan.history

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class HistoryScanActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return HistoryScanFragmentActionBar.newInstance(startupOption)
    }
}