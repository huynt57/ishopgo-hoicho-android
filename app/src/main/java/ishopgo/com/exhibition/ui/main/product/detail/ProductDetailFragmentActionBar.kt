package ishopgo.com.exhibition.ui.main.product.detail

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class ProductDetailFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): ProductDetailFragmentActionBar {
            val fragment = ProductDetailFragmentActionBar()
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

        val fragment = ProductDetailFragment.newInstance(arguments ?: Bundle())
        childFragmentManager.beginTransaction()
                .replace(R.id.view_main_content, fragment)
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chi tiết sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }

}
