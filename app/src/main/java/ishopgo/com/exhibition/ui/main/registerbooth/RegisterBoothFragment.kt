package ishopgo.com.exhibition.ui.main.registerbooth

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.District
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.login.LoginActivity
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.main.salepoint.DistrictAdapter
import kotlinx.android.synthetic.main.fragment_register_booth.*

class RegisterBoothFragment : BaseFragment() {
    companion object {
        fun newInstance(params: Bundle): RegisterBoothFragment {
            val fragment = RegisterBoothFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private lateinit var viewModel: RegisterBoothViewModel
    private val adapterRegion = RegionAdapter()
    private val adapterDistrict = DistrictAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register_booth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_setting_booth_name.setText(UserDataManager.currentUserName)
        tv_setting_booth_hotline.setText(UserDataManager.currentUserPhone)
        tv_setting_booth_city.setOnClickListener { getRegion(tv_setting_booth_city) }
        tv_setting_booth_district.setOnClickListener { getDistrict(tv_setting_booth_district) }

        btn_create_booth.setOnClickListener {
            if (isRequiredFieldsValid(tv_setting_booth_name.text.toString())) {
                showProgressDialog()
                viewModel.registerBooth(tv_setting_booth_name.text.toString(), tv_setting_booth_hotline.text.toString(),
                        tv_setting_booth_introduction.text.toString(), tv_setting_booth_infor.text.toString(),
                        tv_setting_booth_city.text.toString(), tv_setting_booth_district.text.toString(), image)
            }
        }

        img_setting_booth.setOnClickListener {
            launchPickPhotoIntent()
        }
    }

    private fun getRegion(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn thành phố")
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
                        data.provinceid?.let { it1 -> viewModel.loadDistrict(it1) }
                        dialog.dismiss()
                        view.text = data.name
                        view.error = null
                    }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(RegisterBoothViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.registerSusscess.observe(this, Observer {
            hideProgressDialog()
            viewModel.logout()
        })

        viewModel.loggedOut.observe(this, Observer {
            toast("Nâng cấp thành công, vui lòng đăng nhập lại để sử dụng hệ thống")
            UserDataManager.deleteUserInfo()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
            startActivity(intent)
            activity?.finish()
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

        viewModel.loadRegion()
    }

    private var image = ""

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    private fun isRequiredFieldsValid(name: String): Boolean {
        if (name.trim().isEmpty()) {
            toast("Tên gian hàng không được để trống")
            tv_setting_booth_name.error = "Trường này còn trống"
            return false
        }
        return true
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
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(img_setting_booth)
        }
    }
}