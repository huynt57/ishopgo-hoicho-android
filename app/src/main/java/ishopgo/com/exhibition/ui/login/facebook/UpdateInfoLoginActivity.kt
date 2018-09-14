package ishopgo.com.exhibition.ui.login.facebook

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class UpdateInfoLoginActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return UpdateInfoLoginFragmentActionBar.newInstance(startupOption)
    }
}