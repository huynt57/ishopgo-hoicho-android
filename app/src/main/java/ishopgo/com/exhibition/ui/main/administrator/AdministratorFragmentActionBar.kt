package ishopgo.com.exhibition.ui.main.administrator

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class AdministratorFragmentActionBar : BaseActionBarFragment() {

    companion object {
        const val TAG = "AdministratorFragment"
        fun newInstance(params: Bundle): AdministratorFragmentActionBar {
            val fragment = AdministratorFragmentActionBar()
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
                .replace(R.id.view_main_content, AdministratorFragment.newInstance(Bundle()), "AdministratorFragment").commit()
    }

    private fun setupToolbars() {
        if (UserDataManager.currentType == "Chủ gian hàng")
            toolbar.setCustomTitle("Quản trị viên gian hàng")
        if (UserDataManager.currentType == "Chủ hội chợ")
            toolbar.setCustomTitle("Phân quyền quản trị")

        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_add_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(AdministratorFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as AdministratorFragment
                shareFragment.openAddAdministrator()
            }
        }
    }
}