package ishopgo.com.exhibition.ui.main.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.community.share.CommunityShareActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {

    private lateinit var viewModel: ProfileViewModel
    private var isEditMode = false
    private var image: String = ""

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
        viewModel.profileUpdated.observe(this, Observer { p ->
            p?.let {
                toast("Cập nhật thành công")
                showData(it)
                stopEditing()
                isEditMode = false
                activity?.setResult(RESULT_OK)
                hideProgressDialog()
            }
        })
        viewModel.userInfo.observe(this, Observer { i ->
            i?.let {
                showData(it)
            }
        })

        viewModel.loadUserProfile()
    }

    private fun showData(profile: ProfileProvider) {
        context?.let {
            Glide.with(it)
                    .load(profile.provideAvatar())
                    .apply(RequestOptions
                            .circleCropTransform()
                            .placeholder(R.drawable.avatar_placeholder)
                            .error(R.drawable.avatar_placeholder))
                    .into(view_avatar)
            view_phone.setText(profile.providePhone())
            view_name.setText(profile.provideName())
            tv_profile_name.text = profile.provideName()
            view_dob.setText(profile.provideDob())
            view_email.setText(profile.provideEmail())
            view_company.setText(profile.provideCompany())
            view_region.setText(profile.provideRegion())
            view_address.setText(profile.provideAddress())
            view_account_type.setText(profile.provideAccountType())
            view_joined_date.setText(profile.provideJoinedDate())
            view_submit.setOnClickListener {
                if (!isEditMode) {
                    startEditing()
                    isEditMode = true
                } else {
                    submitChanges(view_name.text.toString(), view_dob.text.toString(), view_email.text.toString(),
                            view_company.text.toString(), view_region.text.toString(), view_address.text.toString())
                }
            }
        }
        chooseProfileOption()
    }

    private fun chooseProfileOption() {
        tv_profile_write.setOnClickListener {
            val intent = Intent(context, CommunityShareActivity::class.java)
            startActivity(intent)
        }
        tv_profile_group.setOnClickListener { toast("Đang phát triển") }
        tv_profile_newsfeed.setOnClickListener { toast("Đang phát triển") }
        tv_profile_setting.setOnClickListener { toast("Đang phát triển") }
    }

    private fun submitChanges(name: String, dob: String, email: String, company: String, region: String, address: String) {
        viewModel.updateProfile(name, dob, email, company, region, address, image)
        showProgressDialog()
    }

    @SuppressLint("SetTextI18n")
    private fun startEditing() {
        view_avatar.setOnClickListener {
            launchPickPhotoIntent()
        }

        tv_profile_write.setOnClickListener(null)
        tv_profile_group.setOnClickListener(null)
        tv_profile_newsfeed.setOnClickListener(null)
        tv_profile_setting.setOnClickListener(null)

        view_name.isFocusable = true
        view_name.isFocusableInTouchMode = true
        view_dob.isFocusable = true
        view_dob.isFocusableInTouchMode = true
        view_email.isFocusable = true
        view_email.isFocusableInTouchMode = true
        view_company.isFocusable = true
        view_company.isFocusableInTouchMode = true
        view_region.isFocusable = true
        view_region.isFocusableInTouchMode = true
        view_address.isFocusable = true
        view_address.isFocusableInTouchMode = true

        view_name.requestFocus()
        val inputMethodManager = view_phone.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view_name, 0)

        view_scrollview.smoothScrollTo(0, 0)

        view_submit.text = "Xong"
    }

    @SuppressLint("SetTextI18n")
    private fun stopEditing() {
        view_avatar.setOnClickListener(null)
        view_name.isFocusable = false
        view_name.isFocusableInTouchMode = false
        view_dob.isFocusable = false
        view_dob.isFocusableInTouchMode = false
        view_email.isFocusable = false
        view_email.isFocusableInTouchMode = false
        view_company.isFocusable = false
        view_company.isFocusableInTouchMode = false
        view_region.isFocusable = false
        view_region.isFocusableInTouchMode = false
        view_address.isFocusable = false
        view_address.isFocusableInTouchMode = false
        chooseProfileOption()

        view_scrollview.smoothScrollTo(0, 0)
        view_submit.text = "Cập nhật"
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
            if (Toolbox.exceedSize(context!!, data.data, (2 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 2 MB. Hãy chọn file khác.")
                return
            }

            image = data.data.toString()

            Glide.with(context)
                    .load(Uri.parse(image))
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder))
                    .into(view_avatar)
        }
    }
}