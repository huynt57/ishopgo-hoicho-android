package ishopgo.com.exhibition.ui.main.stamp.buystamp

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class BuyStampFragmentActionBar : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): BuyStampFragmentActionBar {
            val fragment = BuyStampFragmentActionBar()
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
                .replace(R.id.view_main_content, BuyStampFragment.newInstance(Bundle())).commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Đặt mua tem")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }
}