package ishopgo.com.exhibition.ui.main.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Profile
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.community.share.CommunityShareActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asStylePhoneNumber
import ishopgo.com.exhibition.ui.main.profile.edit.ProfileEditActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {

    private lateinit var viewModel: ProfileViewModel
    private var image: String = ""
    private var data: Profile? = null

    companion object {
        const val TAG = "ProfileFragment"
        fun newInstance(params: Bundle): ProfileFragment {
            val fragment = ProfileFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
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

        viewModel.userInfo.observe(this, Observer { i ->
            i?.let {
                showData(it)
            }
        })

        viewModel.profileUpdated.observe(this, Observer { p ->
            p?.let {
                toast("Cập nhật thành công")
                showData(it)
                activity?.setResult(Activity.RESULT_OK)
                hideProgressDialog()
            }
        })

        viewModel.loadUserProfile()
    }

    private fun showData(profile: Profile) {
        val convert = ProfileConverter().convert(profile)
        data = profile
        context?.let {
            Glide.with(it)
                    .load(convert.provideAvatar())
                    .apply(RequestOptions
                            .circleCropTransform()
                            .placeholder(R.drawable.avatar_placeholder)
                            .error(R.drawable.avatar_placeholder))
                    .into(view_avatar)
            tv_profile_name.text = convert.provideName()

            val builder = StringBuilder()
            builder.append("${convert.providePhone()}")
            builder.append("<br>")
            builder.append("${convert.provideDob()}")
            builder.append("<br>")
            builder.append("${convert.provideEmail()}")
            builder.append("<br>")
            builder.append("${convert.provideCompany()}")
            builder.append("<br>")
            builder.append("${convert.provideRegion()}")
            builder.append("<br>")
            builder.append("${convert.provideAddress()}")
            builder.append("<br>")
            builder.append("${convert.provideAccountType()}")
            builder.append("<br>")
            builder.append("${convert.provideJoinedDate()}")
            builder.append("<br>")
            builder.append("${convert.provideIntroduction()}")

            tv_profile_info.text = builder.toString().asHtml()

        }

        chooseProfileOption()
    }

    interface ProfileProvider {

        fun provideAvatar(): String
        fun providePhone(): CharSequence
        fun provideName(): CharSequence
        fun provideDob(): CharSequence
        fun provideEmail(): CharSequence
        fun provideCompany(): CharSequence
        fun provideRegion(): CharSequence
        fun provideAddress(): CharSequence
        fun provideAccountType(): CharSequence
        fun provideJoinedDate(): CharSequence
        fun provideIntroduction(): CharSequence

    }

    class ProfileConverter : Converter<Profile, ProfileProvider> {

        override fun convert(from: Profile): ProfileProvider {
            return object : ProfileProvider {
                override fun provideAccountType(): CharSequence {
                    return "Loại tài khoản: <b>${from.typeTextExpo ?: ""}</b>"
                }

                override fun provideIntroduction(): CharSequence {
                    return "Giới thiệu: <b>${from.introduction ?: ""}</b>"
                }

                override fun provideName(): CharSequence {
                    return from.name ?: ""
                }

                override fun providePhone(): CharSequence {
                    return "Số điện thoại: <b>${from.phone?.asStylePhoneNumber() ?: ""}</b>"
                }

                override fun provideAvatar(): String {
                    return from.image ?: ""
                }

                override fun provideDob(): CharSequence {
                    return "Ngày sinh: <b>${from.birthday?.asDate()}</b>"
                }

                override fun provideEmail(): CharSequence {
                    return "Email: <b>${from.email ?: ""}</b>"
                }

                override fun provideCompany(): CharSequence {
                    return "Công ty: <b>${from.company ?: ""}</b>"
                }

                override fun provideRegion(): CharSequence {
                    return "Khu vực: <b>${from.district ?: ""} - ${from.region ?: ""}</b>"
                }

                override fun provideAddress(): CharSequence {
                    return "Địa chỉ: <b>${from.address ?: ""}</b>"
                }

                override fun provideJoinedDate(): CharSequence {
                    return "Ngày tham gia: <b>${from.createdAt?.asDate()}</b>"
                }
            }
        }
    }

    private fun chooseProfileOption() {
        tv_profile_write_post.setOnClickListener {
            val intent = Intent(context, CommunityShareActivity::class.java)
            startActivityForResult(intent, Const.RequestCode.RC_ADD_NEW)
        }
        tv_profile_group.setOnClickListener { toast("Đang phát triển") }
        tv_profile_setting.setOnClickListener { showDialogSetting() }
    }

    fun openProfileEditActivity() {
        if (data != null) {
            val intent = Intent(context, ProfileEditActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
            startActivityForResult(intent, Const.RequestCode.UPDATE_PROFILE)
        } else toast("Không thể cập nhật tài khoản")
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogSetting() {
        context?.let {
            context?.let {
                val dialog = MaterialDialog.Builder(it)
                        .customView(R.layout.dialog_profile_setting, false)
                        .negativeText("Đóng")
                        .onNegative { dialog, _ -> dialog.dismiss() }
                        .autoDismiss(false)
                        .canceledOnTouchOutside(false)
                        .build()

                val tv_change_name = dialog.findViewById(R.id.tv_change_name) as TextView
                val tv_change_avatar = dialog.findViewById(R.id.tv_change_avatar) as TextView
                val tv_change_banner = dialog.findViewById(R.id.tv_change_banner) as TextView
                val tv_update_profile = dialog.findViewById(R.id.tv_update_profile) as TextView

                tv_change_name.setOnClickListener {
                    showDialogChangeName()
                    dialog.dismiss()
                }
                tv_change_avatar.setOnClickListener {
                    launchPickPhotoIntent()
                    dialog.dismiss()
                }
                tv_change_banner.setOnClickListener {
                    toast("Đang phát triển")
                    dialog.dismiss()
                }
                tv_update_profile.setOnClickListener {
                    openProfileEditActivity()
                    dialog.dismiss()
                }

                val window = dialog.window
                if (window != null) {
                    window.attributes.windowAnimations = R.style.BottomDialog
                    window.setGravity(Gravity.BOTTOM)
                }
                dialog.show()
            }
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
                        submitChanges(edit_profile_name.text.toString(), "", "",
                                "", "", "", "", "")
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
        if (resultCode == Activity.RESULT_OK && requestCode == Const.RequestCode.RC_PICK_IMAGE && null != data) {
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

            viewModel.updateProfile("", "", "",
                    "", "", "", "", "", data.data.toString())
            showProgressDialog()
        }

        if (resultCode == Activity.RESULT_OK && requestCode == Const.RequestCode.UPDATE_PROFILE) {
            viewModel.loadUserProfile()
        }
    }
}