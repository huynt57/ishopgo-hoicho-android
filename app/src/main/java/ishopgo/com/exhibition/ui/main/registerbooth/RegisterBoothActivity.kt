package ishopgo.com.exhibition.ui.main.registerbooth

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class RegisterBoothActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return RegisterBoothFragmentActionBar.newInstance(startupOption)
    }
}