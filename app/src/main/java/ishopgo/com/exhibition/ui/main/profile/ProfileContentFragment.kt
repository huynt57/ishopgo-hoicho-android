package ishopgo.com.exhibition.ui.main.profile

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.RequestParams
import ishopgo.com.exhibition.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileContentFragment : BaseFragment() {

    private lateinit var viewModel: ProfileViewModel
    private var isEditMode = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ProfileViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.profileUpdated.observe(this, Observer { p ->
            p?.let {
                toast("Cập nhật thành công")
                showData(it)
                stopEditing()
                isEditMode = false
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
                    .load(RequestOptions
                            .circleCropTransform()
                            .placeholder(R.drawable.avatar_placeholder)
                            .error(R.drawable.avatar_placeholder))
                    .into(view_avatar)
            view_phone.setText(profile.providePhone())
            view_name.setText(profile.provideName())
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
                    submitChanges()
                }
            }
        }

    }

    private fun submitChanges() {
        viewModel.updateProfile(RequestParams())
    }

    private fun startEditing() {
        view_avatar.setOnClickListener(null)
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

        view_scrollview.smoothScrollTo(0, 0)
        view_submit.text = "Cập nhật"
    }
}
