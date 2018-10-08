package ishopgo.com.exhibition.ui.login.facebook

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.facebook.AccessToken
import com.facebook.Profile
import com.facebook.login.LoginManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.LoginFacebook
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.observable
import ishopgo.com.exhibition.ui.login.LoginViewModel
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.main.MainActivity
import ishopgo.com.exhibition.ui.main.salepoint.DistrictAdapter
import kotlinx.android.synthetic.main.fragment_login_facebook.*
import java.util.concurrent.TimeUnit

class UpdateInfoLoginFragment : BaseFragment() {
    private lateinit var viewModel: LoginViewModel
    private val adapterRegion = RegionAdapter()
    private val adapterDistrict = DistrictAdapter()
    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_facebook, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposables.add(edit_facebook_sdt.observable()
                .debounce(600, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    reloadData = true
                    if (!it.isEmpty()) {
                        viewModel.loadUserByPhone(it)
                    } else {
                        showInfo(null)
                    }
                })
        btn_login_fb.setOnClickListener {
            if (isRequiredFieldsValid(edit_facebook_sdt.text.toString(), edit_facebook_matKhau.text.toString(), edit_facebook_nhapLai_matKhau.text.toString(), edit_facebook_thanhPho.text.toString(), edit_facebook_quanHuyen.text.toString(), UserDataManager.currentUserFacebookId, edit_facebook_ten.text.toString())) {
                showProgressDialog()
                viewModel.updateInfoFacebook(edit_facebook_sdt.text.toString(), edit_facebook_matKhau.text.toString(), edit_facebook_thanhPho.text.toString(),
                        edit_facebook_quanHuyen.text.toString(), edit_facebook_diaChi.text.toString(), UserDataManager.currentUserFacebookId, UserDataManager.currentUserAvatar, edit_facebook_ten.text.toString())
            }
        }

        edit_facebook_thanhPho.setOnClickListener {
            getRegion(edit_facebook_thanhPho)
        }

        edit_facebook_quanHuyen.setOnClickListener {
            getDistrict(edit_facebook_quanHuyen)
        }
    }

    fun logoutFacebook() {
        hideProgressDialog()
        UserDataManager.deleteUserInfo()
        toast("Đăng xuất thành công")
        if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            LoginManager.getInstance().logOut()
        }
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
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

        viewModel.loadRegion.observe(this, Observer { p ->
            p?.let {
                adapterRegion.replaceAll(it)
            }
        })

        viewModel.loadDistrict.observe(this, Observer { p ->
            p?.let {
                adapterDistrict.replaceAll(it)
            }
        })

        viewModel.updateInfoFb.observe(this, Observer { p ->
            p?.let {
                hideProgressDialog()
                UserDataManager.passLoginFacebook = true

                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        })

        viewModel.getUserByPhone.observe(this, Observer { p ->
            p?.let {
                showInfo(it)
            }
        })

        viewModel.loadRegion()
    }

    private fun showInfo(data: PhoneInfo?) {
        edit_facebook_ten.setText(data?.name ?: "")
        edit_facebook_thanhPho.setText(data?.region ?: "")
        edit_facebook_quanHuyen.setText(data?.district ?: "")
        edit_facebook_diaChi.setText(data?.address ?: "")
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
                    dialog.dismiss()
                    data.provinceid?.let { it1 -> viewModel.loadDistrict(it1) }
                    edit_facebook_quanHuyen.visibility = View.VISIBLE
                    view.text = data.name
                    view.error = null
                }
            }
            dialog.show()
        }
    }

    private fun getDistrict(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn quận huyện")
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

            rv_search.adapter = adapterDistrict
            adapterDistrict.listener = object : ClickableAdapter.BaseAdapterAction<District> {
                override fun click(position: Int, data: District, code: Int) {
                    dialog.dismiss()
                    view.text = data.name
                    view.error = null
                }
            }
            dialog.show()
        }
    }

    private fun isRequiredFieldsValid(sdt: String, matKhau: String, nhapLaiMk: String, thanhPho: String, quanHuyen: String, facebookId: String, ten: String): Boolean {
        if (sdt.trim().isEmpty()) {
            toast("Số điện thoại không được để trống")
            edit_facebook_sdt.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_facebook_sdt)

            return false
        }

        if (matKhau.trim().isEmpty()) {
            toast("Mật khẩu không được để trống")
            edit_facebook_matKhau.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_facebook_matKhau)

            return false
        }

        if (nhapLaiMk.trim().isEmpty()) {
            toast("Mật khẩu không được để trống")
            edit_facebook_nhapLai_matKhau.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_facebook_nhapLai_matKhau)

            return false
        }

        if (matKhau != nhapLaiMk) {
            toast("Mật khẩu phải giống nhau")
            edit_facebook_nhapLai_matKhau.error = "Vui lòng nhập lại"
            requestFocusEditText(edit_facebook_nhapLai_matKhau)

            return false
        }

        if (ten.trim().isEmpty()) {
            toast("Họ và tên không được để trống")
            edit_facebook_ten.error = getString(R.string.error_field_required)
            requestFocusEditText(edit_facebook_ten)

            return false
        }

        if (thanhPho.trim().isEmpty()) {
            toast("Thành phố không được để trống")
            edit_facebook_thanhPho.error = "Trường này còn trống"
            requestFocusEditText(edit_facebook_thanhPho)
            return false
        }

        if (quanHuyen.trim().isEmpty()) {
            toast("Quận huyện không được để trống")
            edit_facebook_quanHuyen.error = "Trường này còn trống"
            requestFocusEditText(edit_facebook_quanHuyen)
            return false
        }

        if (facebookId.trim().isEmpty()) {
            toast("Có lỗi xảy ra, vui lòng đăng nhập lại")
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
        const val TAG = "UpdateInfoLoginFragment"
        fun newInstance(params: Bundle): UpdateInfoLoginFragment {
            val fragment = UpdateInfoLoginFragment()
            fragment.arguments = params

            return fragment
        }
    }
}