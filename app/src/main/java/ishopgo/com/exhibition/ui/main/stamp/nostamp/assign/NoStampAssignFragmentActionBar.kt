package ishopgo.com.exhibition.ui.main.stamp.nostamp.assign

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class NoStampAssignFragmentActionBar : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): NoStampAssignFragmentActionBar {
            val fragment = NoStampAssignFragmentActionBar()
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
                .replace(R.id.view_main_content, NoStampAssignFragment.newInstance(arguments
                        ?: Bundle()), "NoStampAssignFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Gán sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }
}