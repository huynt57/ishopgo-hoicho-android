package ishopgo.com.exhibition.ui.main.salepoint

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class SalePointActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return SalePointFragmentActionBar.newInstance(startupOption)
    }
}