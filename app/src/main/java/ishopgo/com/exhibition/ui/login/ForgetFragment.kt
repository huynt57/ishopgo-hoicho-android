package ishopgo.com.exhibition.ui.login

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_forget_password.*
import java.io.IOException

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

                viewModel.accountForget(tv_forget_phone.text.toString())
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(LoginViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.forgetSentSuccess.observe(this, Observer {
            hideProgressDialog()
            toast("Đã gửi yêu cầu, vui lòng kiểm tra tin nhắn điện thoại")
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
}