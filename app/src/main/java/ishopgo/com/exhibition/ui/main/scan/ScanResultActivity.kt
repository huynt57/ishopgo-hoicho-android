package ishopgo.com.exhibition.ui.main.scan

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class ScanResultActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return ScanResultFragment.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }
}