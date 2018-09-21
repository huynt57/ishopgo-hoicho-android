package ishopgo.com.exhibition.ui.main.stamp.buystamp.update.history

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class BuyStampHistoryFragmentActionBar : BaseActionBarFragment(), BackpressConsumable {
    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
    }

    companion object {

        fun newInstance(params: Bundle): BuyStampHistoryFragmentActionBar {
            val fragment = BuyStampHistoryFragmentActionBar()
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
                .replace(R.id.view_main_content, BuyStampHistoryFragment.newInstance(arguments
                        ?: Bundle())).commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Lịch sử cập nhật")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.onBackPressed() }

    }
}