package ishopgo.com.exhibition.ui.login

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by hoangnh on 4/24/2018.
 */
class LoginActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        val phone = intent.getStringExtra("phone")
        return if (phone == null || phone == "") LoginFragment.newInstance("")
        else LoginFragment.newInstance(phone)
    }
}