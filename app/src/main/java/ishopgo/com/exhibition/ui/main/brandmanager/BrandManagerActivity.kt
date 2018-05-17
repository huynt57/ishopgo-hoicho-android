package ishopgo.com.exhibition.ui.main.brandmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class BrandManagerActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return BrandManagerFragmentActionBar.newInstance(startupOption)
    }
}