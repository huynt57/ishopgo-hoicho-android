package ishopgo.com.exhibition.ui.main.administrator.add

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class AdministratorAddActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return AdministratorAddFragmentActionBar.newInstance(startupOption)
    }
}