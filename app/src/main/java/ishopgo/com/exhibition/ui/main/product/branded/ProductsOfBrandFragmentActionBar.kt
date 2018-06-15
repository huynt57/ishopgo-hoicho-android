package ishopgo.com.exhibition.ui.main.product.branded

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ProductsOfBrandFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): ProductsOfBrandFragmentActionBar {
            val fragment = ProductsOfBrandFragmentActionBar()
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
                .replace(R.id.view_main_content, ProductsOfBrandFragment.newInstance(arguments
                        ?: Bundle()))
                .commit()
    }

    private fun setupToolbars() {
        val title = arguments?.getString(Const.TransferKey.EXTRA_TITLE) ?: ""
        toolbar.setCustomTitle(if (title.isBlank()) "Sản phẩm cùng thương hiệu" else title)
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }

}