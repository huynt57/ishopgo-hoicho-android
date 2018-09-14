package ishopgo.com.exhibition.ui.login.facebook

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class UpdateInfoLoginFragmentActionBar : BaseActionBarFragment() {

    companion object {
        fun newInstance(params: Bundle): UpdateInfoLoginFragmentActionBar {
            val fragment = UpdateInfoLoginFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, UpdateInfoLoginFragment.newInstance(Bundle()), "UpdateInfoLoginFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Cập nhật thông tin cá nhân")

        toolbar.leftButton(R.drawable.ic_logout_fb)
        toolbar.setLeftButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(UpdateInfoLoginFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as UpdateInfoLoginFragment
                shareFragment.logoutFacebook()
            }
        }
    }
}