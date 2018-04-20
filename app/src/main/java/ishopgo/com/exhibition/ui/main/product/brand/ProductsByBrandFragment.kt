package ishopgo.com.exhibition.ui.main.product.brand

import android.os.Bundle
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class ProductsByBrandFragment: BaseActionBarFragment() {

    companion object {
        fun newInstance(params: Bundle): ProductsByBrandFragment {
            val fragment = ProductsByBrandFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_products_by_brand
    }

}