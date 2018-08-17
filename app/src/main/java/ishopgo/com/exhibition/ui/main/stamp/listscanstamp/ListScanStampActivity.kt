package ishopgo.com.exhibition.ui.main.stamp.listscanstamp

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class ListScanStampActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return ListScanStampFragmentActionBar.newInstance(startupOption)
    }
}