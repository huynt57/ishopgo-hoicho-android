package ishopgo.com.exhibition.ui.main.visitors

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class VisitorsFragmentActionBar : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): VisitorsFragmentActionBar {
            val fragment = VisitorsFragmentActionBar()
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
                .replace(R.id.view_main_content, VisitorsFragment.newInstance(Bundle())).commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Khách hàng quan tâm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }
}