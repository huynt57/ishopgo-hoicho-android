package ishopgo.com.exhibition.ui.main.boothmanager.add_booth

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class BoothManagerAddActivity  : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return BoothManagerFragmentAddActionBar.newInstance(startupOption)
    }
}