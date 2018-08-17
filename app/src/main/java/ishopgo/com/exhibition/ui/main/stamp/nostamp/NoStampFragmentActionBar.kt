package ishopgo.com.exhibition.ui.main.stamp.nostamp

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class NoStampFragmentActionBar : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): NoStampFragmentActionBar {
            val fragment = NoStampFragmentActionBar()
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
                .replace(R.id.view_main_content, NoStampFragment.newInstance(Bundle()), "NoStampFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Quản lý lô tem")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_add_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(NoStampFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as NoStampFragment
                shareFragment.openAddActivity()
            }
        }
    }
}