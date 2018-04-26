package ishopgo.com.exhibition.ui.login

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.MemoryFile
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.MainActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_signup.*
import java.io.IOException

/**
 * Created by hoangnh on 4/24/2018.
 */
class SignupFragment : BaseFragment() {

    companion object {
        fun newInstance(type_Register: Int): SignupFragment {
            val fragment = SignupFragment()
            fragment.type_Register = type_Register
            return fragment
        }

        const val REGISTER_STORE = 0
        const val REGISTER_MEMBER = 1
    }

    private var type_Register: Int = 0
    private lateinit var viewModel: LoginViewModel
    private val adapterRegion = RegionAdapter()

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

        if (type_Register == REGISTER_MEMBER) textView.text = "Đăng ký thành viên"
        if (type_Register == REGISTER_STORE) textView.text = "Đăng ký gian hàng"

        btn_signup.setOnClickListener {
            if (checkRequireFields(tv_signup_phone.text.toString(), tv_signup_mail.text.toString(), tv_signup_name.text.toString(),
                            tv_signup_region.text.toString(), tv_signup_address.text.toString(), tv_signup_password.text.toString(),
                            tv_signup_retry_password.text.toString())) {
                showProgressDialog()

                viewModel.accountRegister(tv_signup_phone.text.toString(), tv_signup_mail.text.toString(), tv_signup_name.text.toString(),
                        tv_signup_company.text.toString(), tv_signup_birthday.text.toString(), tv_signup_region.text.toString(),
                        tv_signup_address.text.toString(), tv_signup_password.text.toString(), type_Register.toString())
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(LoginViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.registerSuccess.observe(this, Observer {
            hideProgressDialog()
            toast("Đăng ký thành công")
            activity?.finish()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        })

        viewModel.loadRegion.observe(this, Observer { p ->
            p?.let {
                if (reloadData)
                    adapterRegion.replaceAll(it)
                else
                    adapterRegion.addAll(it)
            }
        })

        viewModel.loadRegion(0, requestRegion)
        reloadData = true
    }

    private fun checkRequireFields(phone: String, email: String, fullname: String, region: String, address: String,
                                   password: String, retry_password: String): Boolean {
        if (phone.trim().isEmpty()) {
            toast("Số điện thoại không được để trống")
            tv_signup_phone.error = "Trường này còn trống"
            return false
        }

        if (region.trim().isEmpty()) {
            toast("Khu vực không được để trống")
            tv_signup_region.error = "Trường này còn trống"
            return false
        }

        if (email.trim().isEmpty()) {
            toast("Email không được để trống")
            tv_signup_mail.error = "Trường này còn trống"
            return false
        }

        if (fullname.trim().isEmpty()) {
            toast("Họ và tên không được để trống")
            tv_signup_name.error = "Trường này còn trống"
            return false
        }

        if (region_id > 0) {
            toast("Họ và tên không được để trống")
            tv_signup_name.error = "Trường này còn trống"
            return false
        }

        if (address.trim().isEmpty()) {
            toast("Địa chỉ không được để trống")
            tv_signup_address.error = "Trường này còn trống"
            return false
        }

        if (password.trim().isEmpty()) {
            toast("Mật khẩu không được để trống")
            tv_signup_password.error = "Trường này còn trống"
            return false
        }

        if (retry_password.trim().isEmpty()) {
            toast("Mật khẩu không được để trống")
            tv_signup_retry_password.error = "Trường này còn trống"
            return false
        }

        if (password != retry_password) {
            toast("Mật khẩu nhập vào không giống nhau")
            tv_signup_password.error = "Mật không không giống nhau"
            tv_signup_retry_password.error = "Mật không không giống nhau"
            return false
        }
        return true
    }

    private var requestRegion = ""
    private var region_id: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    private fun getRegion(view: TextView) {
        requestRegion = ""
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
            val edt_search = dialog.findViewById(R.id.edt_search) as TextInputEditText
            edt_search.hint = "Nhập khu vực"
            edt_search.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    requestRegion = s.toString()
                    handler.removeCallbacks(searchRunnable)
                    handler.postDelayed(searchRunnable, 300)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_search.layoutManager = layoutManager
            rv_search.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    reloadData = false
                    viewModel.loadRegion(totalItemsCount, requestRegion)
                }
            })
            rv_search.adapter = adapterRegion
            adapterRegion.listener = object : ClickableAdapter.BaseAdapterAction<Region> {
                override fun click(position: Int, data: Region, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        view.text = data.name
                    }
                }
            }
            dialog.show()
        }
    }

    private var searchRunnable = object : Runnable {
        override fun run() {
            // perform new search, remove all previous
            handler.removeCallbacks(this)

            // real perform search
            reloadData = true
            viewModel.loadRegion(0, requestRegion)
        }
    }
}