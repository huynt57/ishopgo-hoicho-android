package ishopgo.com.exhibition.ui.main.product.detail.fulldetail

import android.os.Bundle
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class FullDetailFragment: BaseActionBarFragment() {

    companion object {
        fun newInstance(params: Bundle): FullDetailFragment {
            val fragment = FullDetailFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_product_full_detail
    }


}