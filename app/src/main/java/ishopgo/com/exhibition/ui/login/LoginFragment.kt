package ishopgo.com.exhibition.ui.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import java.io.IOException

/**
 * Created by hoangnh on 4/24/2018.
 */
class LoginFragment : BaseFragment() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_login_back.setOnClickListener {
            activity?.finish()
        }

        btn_login.setOnClickListener {
            if (checkRequireFields(tv_account.text.toString(), tv_password.text.toString())) {
                showProgressDialog()
                viewModel.accountLogin(tv_account.text.toString(), tv_password.text.toString())
            }
        }

        tv_forget_password.setOnClickListener {
            val intent = Intent(context, ForgetActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(LoginViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.loginSuccess.observe(this, Observer {
            hideProgressDialog()
            toast("Đăng nhập thành công")
            activity?.finish()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        })
    }

    private fun checkRequireFields(name: String, title: String): Boolean {
        if (name.trim().isEmpty()) {
            toast("Tài khoản không được để trống")
            tv_account.error = "Trường này còn trống"
            return false
        }

        if (title.trim().isEmpty()) {
            toast("Mật khẩu không được để trống")
            tv_password.error = "Trường này còn trống"
            return false
        }
        return true
    }
}