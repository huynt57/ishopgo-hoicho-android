package ishopgo.com.exhibition.ui.community.share

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*


/**
 * Created by hoangnh on 4/26/2018.
 */
class CommunityShareFragmentActionBar : BaseActionBarFragment() {

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, CommunityShareFragment.newInstance(Bundle()),
                        "CommunityShareFragmentActionBar").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chia sẻ bài viết")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
        toolbar.rightButton(R.drawable.ic_send_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(CommunityShareFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as CommunityShareFragment
                shareFragment.sentShareCommunity()
            }
        }
    }
}