package ishopgo.com.exhibition.ui.main.membermanager.deletedmember

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class DeletedMemberFragmentActionBar: BaseActionBarFragment() {

    companion object {
        const val TAG = "MemberManagerFragment"
        fun newInstance(params: Bundle): DeletedMemberFragmentActionBar {
            val fragment = DeletedMemberFragmentActionBar()
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
                .replace(R.id.view_main_content, DeletedMemberFragment(), "DeletedMemberFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Khôi phục thành viên")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_filter)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(DeletedMemberFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as DeletedMemberFragment
                shareFragment.performFilter()
            }
        }
    }
}