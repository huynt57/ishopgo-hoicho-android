package ishopgo.com.exhibition.ui.main.brandmanager.update

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class BrandManagerUpdateActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return BrandManagerUpdateFragmentActionBar.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }
}