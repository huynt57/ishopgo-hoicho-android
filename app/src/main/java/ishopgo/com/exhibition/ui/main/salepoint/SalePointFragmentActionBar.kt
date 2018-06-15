package ishopgo.com.exhibition.ui.main.salepoint

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class SalePointFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): SalePointFragmentActionBar {
            val fragment = SalePointFragmentActionBar()
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
                .replace(R.id.view_main_content, SalePointFragment.newInstance(Bundle()), "SalePointFragment")
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Quản lý điểm bán")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
        toolbar.rightButton(R.drawable.ic_add_highlight_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(SalePointFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as SalePointFragment
                shareFragment.openAddSalePoint()
            }
        }
    }

}