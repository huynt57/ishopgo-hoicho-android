package ishopgo.com.exhibition.ui.main.salepoint.add

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class SalePointAddActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return SalePointAddFragmentActionBar.newInstance(startupOption)
    }
}