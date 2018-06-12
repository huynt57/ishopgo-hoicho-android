package ishopgo.com.exhibition.ui.community

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by hoangnh on 4/26/2018.
 */
class CommunityFragmentActionBar : BaseActionBarFragment(), BackpressConsumable {

    override fun onBackPressConsumed(): Boolean {
        return false
    }

    companion object {

        fun newInstance(params: Bundle): CommunityFragmentActionBar {
            val fragment = CommunityFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Cộng đồng")
        toolbar.rightButton(R.drawable.ic_search_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(CommunityFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as CommunityFragment
                shareFragment.openDialogSearch()
            }
        }

        toolbar.rightButton2(R.drawable.ic_notifications_green_24dp)
        toolbar.setRight2ButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(CommunityFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as CommunityFragment
                shareFragment.openNotificationActivity()
            }
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_single_content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, CommunityFragment(), "CommunityFragment")
                .commit()
    }
}