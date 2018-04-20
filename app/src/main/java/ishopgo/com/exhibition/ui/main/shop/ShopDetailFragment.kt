package ishopgo.com.exhibition.ui.main.shop

import android.os.Bundle
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ShopDetailFragment: BaseActionBarFragment() {

    companion object {
        fun newInstance(params: Bundle): ShopDetailFragment {
            val fragment = ShopDetailFragment()
            fragment.arguments = params
            return fragment
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_shop_detail
    }


}