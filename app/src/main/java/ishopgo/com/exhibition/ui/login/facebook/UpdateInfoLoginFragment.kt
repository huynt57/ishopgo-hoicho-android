package ishopgo.com.exhibition.ui.login.facebook

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login_facebook.*

class UpdateInfoLoginFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_facebook, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_login_fb.setOnClickListener {
            if (isRequiredFieldsValid(edit_product_sdt.text.toString(), edit_product_matKhau.text.toString(), edit_product_thanhPho.text.toString(), edt_product_quanHuyen.text.toString())) {
//                showProgressDialog()

            }
        }
    }

    private fun isRequiredFieldsValid(sdt: String, matKhau: String, thanhPho: String, quanHuyen: String): Boolean {
        if (sdt.trim().isEmpty()) {
            toast("Số điện thoại không được để trống")
            edit_product_sdt.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_product_sdt)

            return false
        }

        if (matKhau.trim().isEmpty()) {
            toast("Tên thương hiệu không được để trống")
            edit_product_matKhau.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_product_matKhau)

            return false
        }

        if (thanhPho.trim().isEmpty()) {
            toast("Thành phố không được để trống")
            edit_product_thanhPho.error = "Trường này còn trống"
            requestFocusEditText(edit_product_thanhPho)
            return false
        }

        if (quanHuyen.trim().isEmpty()) {
            toast("Quận huyện không được để trống")
            edt_product_quanHuyen.error = "Trường này còn trống"
            requestFocusEditText(edt_product_quanHuyen)
            return false
        }

        return true
    }

    private fun requestFocusEditText(view: View) {
        view.requestFocus()
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    companion object {
        fun newInstance(params: Bundle): UpdateInfoLoginFragment {
            val fragment = UpdateInfoLoginFragment()
            fragment.arguments = params

            return fragment
        }
    }
}