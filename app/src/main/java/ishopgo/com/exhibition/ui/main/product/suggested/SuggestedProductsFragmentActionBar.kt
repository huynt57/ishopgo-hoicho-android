package ishopgo.com.exhibition.ui.main.product.suggested

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class SuggestedProductsFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): SuggestedProductsFragmentActionBar {
            val fragment = SuggestedProductsFragmentActionBar()
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
                .replace(R.id.view_main_content, SuggestedFragment())
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Sản phẩm gợi ý")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }

}