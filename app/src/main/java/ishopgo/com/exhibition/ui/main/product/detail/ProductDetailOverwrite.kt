package ishopgo.com.exhibition.ui.main.product.detail

import android.view.View
import ishopgo.com.exhibition.domain.response.ProductDetail

/**
 * Created by xuanhong on 7/13/18. HappyCoding!
 */
abstract class ProductDetailOverwrite {

    abstract fun handleInOtherFlavor(rootView: View, detail: ProductDetail)

}