package ishopgo.com.exhibition.ui.main.scan.history

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class HistoryScanFragmentActionBar : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): HistoryScanFragmentActionBar {
            val fragment = HistoryScanFragmentActionBar()
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
                .replace(R.id.view_main_content, HistoryScanTabFragment.newInstance(Bundle()), "QuestionFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Lịch sử quét mã")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }
}