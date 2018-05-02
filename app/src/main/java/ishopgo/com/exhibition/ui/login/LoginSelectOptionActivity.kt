package ishopgo.com.exhibition.ui.login

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by hoangnh on 4/24/2018.
 */
class LoginSelectOptionActivity : BaseSingleFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserDataManager.currentUserAvatar = "https://lh3.googleusercontent.com/-B212qdip-ls/V1a_aouWG1I/AAAAAAAAAfA/pqqn9gV9tcIos_ybMhM_xLmFORG_ZHEowCEwYBhgL/w278-h280-p/10275486_934954516552276_3867031212727261639_o.jpg"
        UserDataManager.currentUserName = "Vương Xuân Hồng"
        UserDataManager.currentUserPhone = "0974427143"
        UserDataManager.currentUserId = 10L
        UserDataManager.appId = "hoichone"

    }

    override fun createFragment(startupOption: Bundle): Fragment {
        return LoginSelectOptionFragment()
    }
}