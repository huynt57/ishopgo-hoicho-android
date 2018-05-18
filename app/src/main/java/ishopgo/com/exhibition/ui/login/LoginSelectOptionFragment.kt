package ishopgo.com.exhibition.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_select_option_login.*

/**
 * Created by hoangnh on 4/24/2018.
 */
class LoginSelectOptionFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_login.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_sigup.setOnClickListener {
            val intent = Intent(context, SignupActivity::class.java)
            startActivity(intent)
        }

        tv_skip_login.setOnClickListener {
            // we pass login screen after click skip
            UserDataManager.passLoginScreen = 1

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_option_login, container, false)
    }
}