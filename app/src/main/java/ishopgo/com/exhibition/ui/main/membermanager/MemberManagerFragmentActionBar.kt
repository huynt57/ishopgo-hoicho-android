package ishopgo.com.exhibition.ui.main.membermanager

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class MemberManagerFragmentActionBar : BaseActionBarFragment() {

    companion object {
        const val TAG = "MemberManagerFragment"
        fun newInstance(params: Bundle): MemberManagerFragmentActionBar {
            val fragment = MemberManagerFragmentActionBar()
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
                .replace(R.id.view_main_content, MemberManagerFragment(), "MemberManagerFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Quản lý thành viên")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_restore_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(MemberManagerFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as MemberManagerFragment
                    shareFragment.openRestoreMember()
            }
        }

        toolbar.rightButton2(R.drawable.ic_filter_highlight_24dp)
        toolbar.setRight2ButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(MemberManagerFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as MemberManagerFragment
                shareFragment.performFilter()
            }
        }
    }
}