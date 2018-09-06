package ishopgo.com.exhibition.ui.main.stamp.stampdistribution

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class StampDistributionFragmentActionBar : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): StampDistributionFragmentActionBar {
            val fragment = StampDistributionFragmentActionBar()
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
                .replace(R.id.view_main_content, StampDistributionFragment.newInstance(Bundle()), "StampDistributionFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Kích hoạt lô tem")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_add_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(StampDistributionFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as StampDistributionFragment
                shareFragment.openAssignNoStamp()
            }
        }
    }
}