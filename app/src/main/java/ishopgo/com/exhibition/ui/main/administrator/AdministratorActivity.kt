package ishopgo.com.exhibition.ui.main.administrator

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class AdministratorActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return AdministratorFragmentActionBar.newInstance(startupOption)
    }
}