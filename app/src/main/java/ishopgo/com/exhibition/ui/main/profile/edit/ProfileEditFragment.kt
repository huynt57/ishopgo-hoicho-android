package ishopgo.com.exhibition.ui.main.profile.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
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
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.main.profile.ProfileViewModel
import ishopgo.com.exhibition.ui.main.salepoint.DistrictAdapter
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class ProfileEditFragment : BaseFragment() {

    private lateinit var viewModel: ProfileViewModel
    private var image: String = ""
    private val adapterRegion = RegionAdapter()
    private val adapterDistrict = DistrictAdapter()

    companion object {
        fun newInstance(params: Bundle): ProfileEditFragment {
            val fragment = ProfileEditFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ProfileViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.profileUpdated.observe(this, Observer { p ->
            p?.let {
                toast("Cập nhật thành công")
                showData(it)
                activity?.setResult(Activity.RESULT_OK)
                hideProgressDialog()
                activity?.finish()
            }
        })
        viewModel.userInfo.observe(this, Observer { i ->
            i?.let {
                showData(it)
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

        viewModel.loadRegion()
        val json: String = arguments?.getString(Const.TransferKey.EXTRA_JSON) ?: ""
        showData(Toolbox.gson.fromJson(json, Profile::class.java))
    }

    private fun showData(profile: Profile) {
        context?.let {
                Glide.with(it)
                        .load(profile.image)
                        .apply(RequestOptions
                                .circleCropTransform()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder))
                        .into(view_avatar)
                tv_profile_name.text = profile.name ?: ""
                view_name.setText(profile.name ?: "")
                view_phone.setText(profile.phone ?: "")
                view_dob.setText(profile.birthday?.asDate() ?: "")
                view_email.setText(profile.email ?: "")
                view_company.setText(profile.company ?: "")
                view_region.setText(profile.region ?: "")
                view_district.setText(profile.district ?: "")
                view_address.setText(profile.address ?: "")
                view_introduction.setText(profile.introduction ?: "")
                view_account_type.setText(profile.typeTextExpo ?: "")
                view_joined_date.setText(profile.createdAt?.asDate() ?: "")

            view_submit.setOnClickListener {
                submitChanges(view_name.text.toString(), view_dob.text.toString(), view_email.text.toString(),
                        view_company.text.toString(), view_region.text.toString(), view_district.text.toString(), view_address.text.toString(), view_introduction.text.toString())
            }

            tv_profile_name.setOnClickListener { showDialogChangeName() }

            view_avatar.setOnClickListener { launchPickPhotoIntent() }

            view_region.setOnClickListener { getRegion(view_region) }
            view_district.setOnClickListener { getDistrict(view_district) }
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
                        dialog.dismiss()
                        data.provinceid?.let { it1 -> viewModel.loadDistrict(it1) }
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

    private fun showDialogChangeName() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Thay đổi tên")
                    .customView(R.layout.dialog_change_profile_name, false)
                    .positiveText("Thay đổi")
                    .onPositive { dialog, which ->
                        val edit_profile_name = dialog.findViewById(R.id.edit_profile_name) as TextInputEditText
                        submitChanges(edit_profile_name.text.toString(), view_dob.text.toString(), view_email.text.toString(),
                                view_company.text.toString(), view_region.text.toString(), view_district.text.toString(), view_address.text.toString(), view_introduction.text.toString())
                        dialog.dismiss()
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val edit_profile_name = dialog.findViewById(R.id.edit_profile_name) as TextInputEditText
            edit_profile_name.setText(UserDataManager.currentUserName)

            dialog.show()
        }
    }

    private fun submitChanges(name: String, dob: String, email: String, company: String, region: String, district: String, address: String, introduction: String) {
        viewModel.updateProfile(name, dob, email, company, region, district, address, introduction, image)
        showProgressDialog()
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Const.RequestCode.RC_PICK_IMAGE && null != data) {
                if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                    toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                    return
                }

                image = data.data.toString()

                Glide.with(context)
                        .load(data.data)
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        )
                        .into(view_avatar)
            }
        }
    }
}