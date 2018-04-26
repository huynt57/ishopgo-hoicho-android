package ishopgo.com.exhibition.ui.main.product.viewed

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ViewedProductsFragmentActionBar : BaseActionBarFragment() {

    companion object {

        fun newInstance(params: Bundle): ViewedProductsFragmentActionBar {
            val fragment = ViewedProductsFragmentActionBar()
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
                .replace(R.id.view_main_content, ViewedFragment())
                .commit()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Sản phẩm đã xem")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }

}