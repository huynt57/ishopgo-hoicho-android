package ishopgo.com.exhibition.ui.main.product.detail.sale_point

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class ProductSalePointFragmentActionBar : BaseActionBarFragment() {
    companion object {
        fun newInstance(params: Bundle): ProductSalePointFragmentActionBar {
            val fragment = ProductSalePointFragmentActionBar()
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
                .replace(R.id.view_main_content, ProductSalePointFragment.newInstance(arguments
                        ?: Bundle()), "ProductSalePointFragment").commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Danh sách điểm bán")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        toolbar.rightButton(R.drawable.ic_add_green_24dp)
        toolbar.setRightButtonClickListener {
            val fragment = childFragmentManager.findFragmentByTag(ProductSalePointFragment.TAG)
            if (fragment != null) {
                val shareFragment = fragment as ProductSalePointFragment
                shareFragment.openAddSalePoint()
            }
        }
    }
}
