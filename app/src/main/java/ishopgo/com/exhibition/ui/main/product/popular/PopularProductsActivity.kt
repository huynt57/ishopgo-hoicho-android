package ishopgo.com.exhibition.ui.main.product.popular

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActivity
import ishopgo.com.exhibition.ui.base.BaseNavigationActivity
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class PopularProductsActivity : BaseNavigationActivity() {

    override fun navigationRes(): Int {
        return R.navigation.nav__popular_products
    }

}