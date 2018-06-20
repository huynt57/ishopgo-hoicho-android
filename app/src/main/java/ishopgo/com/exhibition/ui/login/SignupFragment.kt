package ishopgo.com.exhibition.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PhoneInfo
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.fragment_signup.*


/**
 * Created by hoangnh on 4/24/2018.
 */
class SignupFragment : BaseFragment() {

    companion object {
        fun newInstance(params: Bundle): SignupFragment {
            val fragment = SignupFragment()
            fragment.arguments = params
            return fragment
        }
    }

    private lateinit var viewModel: LoginViewModel
    private val adapterRegion = RegionAdapter()
    private var image: String = ""
    private var searchKeyword = ""
    private val handler = Handler()
    private val searchRunnable = object : Runnable {
        override fun run() {
            handler.removeCallbacks(this)

            reloadData = true
            if (!searchKeyword.isEmpty()) {
                viewModel.loadUserByPhone(searchKeyword)
            } else {
                fillInfo(null)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_signup_back.setOnClickListener {
            activity?.finish()
        }

        tv_signup_region.setOnClickListener {
            getRegion(tv_signup_region)
        }

        btn_signup.setOnClickListener {
            signupAccount()
        }

        img_signup_avatar.setOnClickListener {
            launchPickPhotoIntent()
        }

        tv_signup_phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchKeyword = s.toString()
                tv_signup_phone.handler.removeCallbacks(searchRunnable)
                tv_signup_phone.handler.postDelayed(searchRunnable, 500)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        tv_signup_retry_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_NULL) {

                val imm: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(tv_signup_retry_password.windowToken, 0)

                signupAccount()
                return@OnEditorActionListener true
            }

            false
        })
    }

    private fun signupAccount() {
        if (checkRequireFields(tv_signup_phone.text.toString(), tv_signup_mail.text.toString(), tv_signup_name.text.toString(),
                        tv_signup_region.text.toString(), tv_signup_address.text.toString(), tv_signup_password.text.toString(),
                        tv_signup_retry_password.text.toString())) {
            showProgressDialog()

            viewModel.registerAccount(tv_signup_phone.text.toString(), tv_signup_mail.text.toString(), tv_signup_name.text.toString(),
                    tv_signup_company.text.toString(), tv_signup_region.text.toString(),
                    tv_signup_address.text.toString(), tv_signup_password.text.toString())
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

        viewModel.registerSuccess.observe(this, Observer {
            hideProgressDialog()
            toast("Đăng ký thành công")
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("phone", tv_signup_phone.text.toString())
            startActivity(intent)
            activity?.finish()
        })

        viewModel.loadRegion.observe(this, Observer { p ->
            p?.let {
                adapterRegion.replaceAll(it)
            }
        })

        viewModel.getUserByPhone.observe(this, Observer { p ->
            p?.let {
                fillInfo(it)
            }
        })

        viewModel.loadRegion()
    }

    private fun checkRequireFields(phone: String, email: String, fullname: String, region: String, address: String,
                                   password: String, retry_password: String): Boolean {

        if (phone.trim().isEmpty()) {
            toast("Số điện thoại không được để trống")
            tv_signup_phone.error = "Trường này còn trống"
            requestFocusEditText(tv_signup_phone)
            return false
        }

        if (email.trim().isEmpty()) {
            toast("Email không được để trống")
            tv_signup_mail.error = "Trường này còn trống"
            requestFocusEditText(tv_signup_mail)
            return false
        }

        if (fullname.trim().isEmpty()) {
            toast("Họ và tên không được để trống")
            tv_signup_name.error = "Trường này còn trống"
            requestFocusEditText(tv_signup_name)
            return false
        }

        if (region.trim().isEmpty()) {
            toast("Khu vực không được để trống")
            tv_signup_region.error = "Trường này còn trống"
            requestFocusEditText(tv_signup_region)
            return false
        }

        if (address.trim().isEmpty()) {
            toast("Địa chỉ không được để trống")
            tv_signup_address.error = "Trường này còn trống"
            requestFocusEditText(tv_signup_address)
            return false
        }

        if (password.trim().isEmpty()) {
            toast("Mật khẩu không được để trống")
            tv_signup_password.error = "Trường này còn trống"
            requestFocusEditText(tv_signup_password)
            return false
        }

        if (retry_password.trim().isEmpty()) {
            toast("Mật khẩu không được để trống")
            tv_signup_retry_password.error = "Trường này còn trống"
            requestFocusEditText(tv_signup_retry_password)
            return false
        }

        if (password != retry_password) {
            toast("Mật khẩu nhập vào không giống nhau")
            tv_signup_password.error = "Mật không không giống nhau"
            tv_signup_retry_password.error = "Mật không không giống nhau"
            requestFocusEditText(tv_signup_password)
            return false
        }
        return true
    }

    private fun getRegion(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn khu vực")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            rv_search.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)

            rv_search.adapter = adapterRegion
            adapterRegion.listener = object : ClickableAdapter.BaseAdapterAction<Region> {
                override fun click(position: Int, data: Region, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        view.text = data.name
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private fun requestFocusEditText(view: View) {
        view.requestFocus()
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                return
            }

            image = data.data.toString()

            Glide.with(context)
                    .load(Uri.parse(image))
                    .apply(RequestOptions.placeholderOf(R.drawable.avatar_placeholder)
                            .error(R.drawable.avatar_placeholder))
                    .into(img_signup_avatar)
        }
    }

    private fun fillInfo(user: PhoneInfo?) {
        tv_signup_mail.setText(user?.email ?: "")
        tv_signup_name.setText(user?.name ?: "")
        tv_signup_region.setText(user?.region ?: "")
        tv_signup_company.setText(user?.company ?: "")
        tv_signup_address.setText(user?.address ?: "")
    }
}