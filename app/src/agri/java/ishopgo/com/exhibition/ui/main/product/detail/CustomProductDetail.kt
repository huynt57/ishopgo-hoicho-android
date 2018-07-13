package ishopgo.com.exhibition.ui.main.product.detail

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.ui.widget.ProductInfo
import kotlinx.android.synthetic.main.fragment_product_detail.view.*

/**
 * Created by xuanhong on 7/13/18. HappyCoding!
 */
class CustomProductDetail : ProductDetailOverwrite() {

    override fun handleInOtherFlavor(rootView: View, detail: ProductDetail) {
        Log.d("Hong", "write more in agri variation: ")
        rootView.apply {
            val linearLayout = view_content_container as LinearLayout

            val productInfo = ProductInfo(rootView.context)
            productInfo.apply {
                label_products_same_shop.text = "new view"
            }
            linearLayout.addView(productInfo)
        }
    }

}