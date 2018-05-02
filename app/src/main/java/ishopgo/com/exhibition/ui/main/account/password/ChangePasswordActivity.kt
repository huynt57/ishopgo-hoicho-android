package ishopgo.com.exhibition.ui.main.account.password

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.community.ChangePasswordFragmentActionBar

/**
 * Created by xuanhong on 4/24/18. HappyCoding!
 */
class ChangePasswordActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return ChangePasswordFragmentActionBar()
    }
}