package ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class IcheckSalePointFragmentActionBar : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): IcheckSalePointFragmentActionBar {
            val fragment = IcheckSalePointFragmentActionBar()
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
                .replace(R.id.view_main_content, IcheckSalePointFragment.newInstance(arguments
                        ?: Bundle()), "IcheckSalePointFragment")
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Danh sách điểm bán")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }
}