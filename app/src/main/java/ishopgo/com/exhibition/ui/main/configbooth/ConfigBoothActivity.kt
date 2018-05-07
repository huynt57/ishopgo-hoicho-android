package ishopgo.com.exhibition.ui.main.configbooth

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by hoangnh on 5/5/2018.
 */
class ConfigBoothActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return ConfigBoothFragmentActionBar()
    }
}