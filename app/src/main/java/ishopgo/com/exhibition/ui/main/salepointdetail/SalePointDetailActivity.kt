package ishopgo.com.exhibition.ui.main.salepointdetail

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class SalePointDetailActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return SalePointDetailFragmentActionBar.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }
}