package ishopgo.com.exhibition.ui.community.notification

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class CommunityNotificationFragmentActionBar : BaseActionBarFragment() {

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, CommunityNotificationFragment.newInstance(Bundle()),
                        "CommunityNotificationFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Danh sách thông báo")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
        toolbar.rightButton(R.drawable.ic_done_all_black_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(CommunityNotificationFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as CommunityNotificationFragment
                shareFragment.marksAllAsRead()
            }
        }
    }
}