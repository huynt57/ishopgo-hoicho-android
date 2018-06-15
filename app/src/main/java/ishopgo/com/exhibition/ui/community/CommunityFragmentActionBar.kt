package ishopgo.com.exhibition.ui.community

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.main.MainViewModel
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

    private lateinit var mainViewModel : MainViewModel

    private fun setupToolbars() {
        toolbar.setCustomTitle("Cộng đồng")
        toolbar.rightButton(R.drawable.ic_search_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(CommunityFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as CommunityFragment
                shareFragment.openDialogSearch()
            }
        }

        toolbar.leftButton(R.drawable.ic_notifications_highlight_24dp)
        toolbar.setLeftButtonClickListener {
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = obtainViewModel(MainViewModel::class.java, true)
        mainViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        mainViewModel.notificationCount.observe(this, Observer { c ->
            c?.let {
                toolbar.leftButton(R.drawable.ic_notifications_highlight_24dp, if (it > 0) it else 0)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, CommunityFragment(), "CommunityFragment")
                .commit()
    }
}