package ishopgo.com.exhibition.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.main.MainActivity

/**
 * Created by hoangnh on 4/24/2018.
 */
class LoginSelectOptionActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        if (UserDataManager.currentUserId > 0) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        return LoginSelectOptionFragment()
    }
}