package ishopgo.com.exhibition.ui.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_forget_password.*

/**
 * Created by hoangnh on 4/24/2018.
 */
class ForgetFragment : BaseFragment() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forget_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_forget_back.setOnClickListener {
            activity?.finish()
        }

        btn_forget_sent.setOnClickListener {
            if (checkRequireFields(tv_forget_phone.text.toString())) {
                showProgressDialog()
                viewModel.getOTP(tv_forget_phone.text.toString())
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(LoginViewModel::class.java, false)
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

        viewModel.changNewPassword.observe(this, Observer {
            hideProgressDialog()
            toast("Đặt lại mật khẩu thành công. Vui lòng đăng nhập.")
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("phone", tv_forget_phone.text.toString())
            startActivity(intent)
            activity?.finish()
        })
    }

    private fun checkRequireFields(phone: String): Boolean {
        if (phone.trim().isEmpty()) {
            toast("Số điện thoại không được để trống")
            tv_forget_phone.error = "Trường này còn trống"
            return false
        }
        return true
    }


    private fun checkRequirePassword(edt_otp: TextInputEditText, edt_password: TextInputEditText, edt_retry_password: TextInputEditText): Boolean {
        if (edt_otp.text.toString().trim().isEmpty()) {
            toast("Vui lòng nhập mã OTP")
            edt_otp.error = "Trường này còn trống"
            return false
        }

        if (edt_password.text.toString().trim().isEmpty()) {
            toast("Mật khẩu không được để trống")
            edt_password.error = "Trường này còn trống"
            return false
        }

        if (edt_retry_password.text.toString().trim().isEmpty()) {
            toast("Mật khẩu không được để trống")
            edt_retry_password.error = "Trường này còn trống"
            return false
        }

        if (edt_password.text.toString() != edt_retry_password.text.toString()) {
            toast("Mật khẩu nhập vào không giống nhau")
            edt_password.error = "Mật không không giống nhau"
            edt_retry_password.error = "Mật không không giống nhau"
            return false
        }
        return true
    }

    private fun dialogChangePassword() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_change_password, false)
                    .title("Đặt lại mật khẩu")
                    .positiveText("Hoàn tất")
                    .onPositive { dialog, _ ->
                        val edit_password = dialog.findViewById(R.id.edit_password) as TextInputEditText
                        val edit_retry_password = dialog.findViewById(R.id.edit_retry_password) as TextInputEditText
                        val edt_forget_password_otp = dialog.findViewById(R.id.edt_forget_password_otp) as TextInputEditText

                        if (checkRequirePassword(edt_forget_password_otp, edit_password, edit_retry_password)) {
                            viewModel.changeNewPassword(tv_forget_phone.text.toString(),
                                    edt_forget_password_otp.text.toString(), edit_password.text.toString())
                            showProgressDialog()
                            dialog.dismiss()
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
                viewModel.getOTP(tv_forget_phone.text.toString())
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}