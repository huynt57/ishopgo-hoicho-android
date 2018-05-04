package ishopgo.com.exhibition.ui.login

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by hoangnh on 4/24/2018.
 */
class LoginFragment : BaseFragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var phone: String

    companion object {
        fun newInstance(phone: String): LoginFragment {
            val f = LoginFragment()
            f.phone = phone
            return f
        }
    }

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
                viewModel.loginAccount(tv_account.text.toString(), tv_password.text.toString())
            }
        }

        tv_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_NULL) {

                val imm: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(tv_password.windowToken, 0)

                if (checkRequireFields(tv_account.text.toString(), tv_password.text.toString())) {
                    showProgressDialog()
                    viewModel.loginAccount(tv_account.text.toString(), tv_password.text.toString())
                }
                return@OnEditorActionListener true
            }

            false
        })

        tv_forget_password.setOnClickListener {
            val intent = Intent(context, ForgetActivity::class.java)
            startActivity(intent)
        }

        if (phone != "") {
            tv_account.setText(phone)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(LoginViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                resolveError(it)
                hideProgressDialog()
            }
        })

        viewModel.loginSuccess.observe(this, Observer { p ->
            p.let {
                hideProgressDialog()
                toast("Đăng nhập thành công")
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
            }
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