package ishopgo.com.exhibition.ui.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.MainActivity
import ishopgo.com.exhibition.ui.survey.SurveyActivity
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by hoangnh on 4/24/2018.
 */
class LoginFragment : BaseFragment() {
    private lateinit var viewModel: LoginViewModel
    private var phone = ""

    companion object {
        fun newInstance(phone: String): LoginFragment {
            val f = LoginFragment()
            f.phone = phone
            return f
        }

        val NOT_SURVEY = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_forget_password.text = "Quên mật khẩu ? <b><font color=\"#01579B\">Tìm lại mật khẩu</font></b>".asHtml()
        btn_signup.text = "Chưa có tài khoản ? <b><font color=\"#ff3d00\">Đăng ký ngay</font></b>".asHtml()

        btn_signup.setOnClickListener {
            val intent = Intent(context, SignupActivity::class.java)
            startActivity(intent)
        }

        tv_skip_login.setOnClickListener {
            // we pass login screen after click skip
            UserDataManager.passLoginScreen = 1

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
            startActivity(intent)
            activity?.finish()
        }

        btn_login.setOnClickListener {
            if (checkRequireFields(tv_account.text.toString(), tv_password.text.toString())) {
                showProgressDialog()
                viewModel.loginAccount(tv_account.text.toString(), tv_password.text.toString())
            }
        }

        tv_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                tv_password.hideKeyboard()

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

        if (phone.isNotEmpty()) {
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
                if (UserDataManager.currentSurveyIsMandatory) {
                    if (UserDataManager.currentType == "Thành viên")
                        viewModel.checkSurvey()
                    else {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                } else {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        })

        viewModel.checkSurveySusscess.observe(this, Observer { p ->
            p.let {
                if (it == NOT_SURVEY) {
                    toast("Bạn chưa làm bài khảo sát")
                    val intent = Intent(context, SurveyActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, "")
                    startActivity(intent)
                    activity?.finish()
                } else {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                    startActivity(intent)
                    activity?.finishAffinity()
                }
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