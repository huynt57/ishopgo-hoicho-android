package ishopgo.com.exhibition.ui.main.product.promotion

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class PromotionProductsFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): PromotionProductsFragmentActionBar {
            val fragment = PromotionProductsFragmentActionBar()
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
                .replace(R.id.view_main_content, PromotionProductsFragment())
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Tìm trong sản phẩm khuyến mại")
        val titleView = toolbar.getTitleView()
        titleView.setBackgroundResource(R.drawable.bg_search_box)
        titleView.setTextColor(resources.getColor(R.color.md_grey_700))
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        titleView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_promotionProductsFragmentActionBar_to_searchPromotionProductsFragment)
        }
        titleView.drawableCompat(0, 0, R.drawable.ic_search_highlight_24dp, 0)
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }

}