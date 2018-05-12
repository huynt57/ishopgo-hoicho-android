package ishopgo.com.exhibition.ui.main.boothmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class BoothManagerActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return BoothManagerFragmentActionBar.newInstance(startupOption)
    }
}