package ishopgo.com.exhibition.ui.filterproduct

import android.view.View
import kotlinx.android.synthetic.main.content_filter_product.view.*

class CustomFilterProduct : FilterProductOverwrite() {
    override fun handleInOtherFlavor(rootView: View) {
        rootView.apply {
//            cv_nksx.visibility = View.VISIBLE
        }
    }
}