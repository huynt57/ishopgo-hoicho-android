package ishopgo.com.exhibition.ui.main.productmanager.add

import android.view.View
import kotlinx.android.synthetic.main.fragment_product_manager_add.view.*

class CustomProductManagerAdd : ProductManagerAddOverwrite() {
    override fun handleInOtherFlavor(rootView: View) {
        rootView.apply {
            linear_agri_product.visibility = View.VISIBLE
            linear_agri.visibility = View.VISIBLE
            linear_scale.visibility = View.GONE
        }
    }
}