package ishopgo.com.exhibition.ui.main.account.password

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.login.LoginActivity
import ishopgo.com.exhibition.ui.main.account.AccountViewModel
import kotlinx.android.synthetic.main.fragment_change_password.*

/**
 * Created by hoangnh on 4/26/2018.
 */
class ChangePasswordFragment : BaseFragment() {

    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_change_password.setOnClickListener {
            if (checkRequireFields(edt_password.text.toString(), edt_retry_password.text.toString())) {
                showProgressDialog()
                viewModel.getOTP(UserDataManager.currentUserPhone)
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(AccountViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.getOTP.observe(this, Observer {
            hideProgressDialog()
            toast("Đã gửi yêu cầu, vui lòng kiểm tra tin nhắn điện thoại")
            dialogChangePassword()
        })

        viewModel.changePassword.observe(this, Observer {
            hideProgressDialog()
            toast("Đổi mật khẩu thành công, vui lòng đăng nhập lại")
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("phone", UserDataManager.currentUserPhone)
            startActivity(intent)
            activity?.finish()
        })
    }

    private fun checkRequireFields(password: String, retry_password: String): Boolean {
        if (password.trim().isEmpty()) {
            toast("Mật khẩu không được để trống")
            edt_password.error = "Trường này còn trống"
            requestFocusEditText(edt_password)
            return false
        }

        if (retry_password.trim().isEmpty()) {
            toast("Mật khẩu không được để trống")
            edt_retry_password.error = "Trường này còn trống"
            requestFocusEditText(edt_retry_password)
            return false
        }

        if (password != retry_password) {
            toast("Mật khẩu nhập vào không giống nhau")
            edt_password.error = "Mật không không giống nhau"
            edt_retry_password.error = "Mật không không giống nhau"
            requestFocusEditText(edt_password)
            return false
        }
        return true
    }

    private fun dialogChangePassword() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_otp, false)
                    .title("Đặt lại mật khẩu")
                    .positiveText("Gửi")
                    .onPositive { dialog, _ ->
                        val edt_forget_password_otp = dialog.findViewById(R.id.edt_forget_password_otp) as TextInputEditText

                        if (checkRequireOTP(edt_forget_password_otp.text.toString(), edt_forget_password_otp)) {
                            viewModel.changePassword(UserDataManager.currentUserPhone,
                                    edt_forget_password_otp.text.toString(), edt_password.text.toString())

                            showProgressDialog()
                        }


                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()

            val tv_retry_otp = dialog.findViewById(R.id.tv_retry_otp) as TextView
            tv_retry_otp.setOnClickListener {
                showProgressDialog()
                viewModel.getOTP(UserDataManager.currentUserPhone)
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun checkRequireOTP(otp: String, view: TextInputEditText): Boolean {
        if (otp.trim().isEmpty()) {
            toast("Mã OTP không được để trống")
            view.error = "Trường này còn trống"
            requestFocusEditText(view)
            return false
        }
        return true
    }

    private fun requestFocusEditText(view: View) {
        view.requestFocus()
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }
}