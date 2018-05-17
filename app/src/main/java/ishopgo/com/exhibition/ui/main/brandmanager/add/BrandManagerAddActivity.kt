package ishopgo.com.exhibition.ui.main.brandmanager.add

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class BrandManagerAddActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return BrandManagerAddFragmentActionBar.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }
}