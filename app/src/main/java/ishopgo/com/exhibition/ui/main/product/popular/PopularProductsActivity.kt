package ishopgo.com.exhibition.ui.main.product.popular

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.FilterProduct.FilterProductFragment
import ishopgo.com.exhibition.ui.FilterProduct.FilterProductViewModel
import ishopgo.com.exhibition.ui.base.BackpressConsumable
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

    private lateinit var viewModel: FilterProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = obtainViewModel(FilterProductViewModel::class.java)
        viewModel.showFragmentFilter.observe(this, Observer {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                    .add(R.id.frame_nav_layout, FilterProductFragment.newInstance(Bundle()))
                    .addToBackStack(FilterProductFragment.TAG)
                    .commit()
        })
    }
}