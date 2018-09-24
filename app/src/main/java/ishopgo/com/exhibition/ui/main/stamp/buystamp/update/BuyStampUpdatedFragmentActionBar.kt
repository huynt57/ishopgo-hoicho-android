package ishopgo.com.exhibition.ui.main.stamp.buystamp.update

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class BuyStampUpdatedFragmentActionBar : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): BuyStampUpdatedFragmentActionBar {
            val fragment = BuyStampUpdatedFragmentActionBar()
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
                .replace(R.id.view_main_content, BuyStampUpdatedFragment.newInstance(arguments
                        ?: Bundle()), "BuyStampUpdatedFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Sửa lô tem")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_history)
        toolbar.setRightButtonClickListener {
                val fragment = childFragmentManager.findFragmentByTag(BuyStampUpdatedFragment.TAG)
                if (fragment != null) {
                    val shareFragment = fragment as BuyStampUpdatedFragment
                    shareFragment.openHistory()
                }
        }
    }
}