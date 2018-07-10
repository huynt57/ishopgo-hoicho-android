package ishopgo.com.exhibition.ui.main.product.popular

import android.arch.lifecycle.Observer
import android.os.Bundle
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.filterproduct.FilterProductFragment
import ishopgo.com.exhibition.ui.filterproduct.FilterProductViewModel
import ishopgo.com.exhibition.ui.base.BaseNavigationActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox

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
            val extra = Bundle()
            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                    .add(R.id.frame_nav_layout, FilterProductFragment.newInstance(extra))
                    .addToBackStack(FilterProductFragment.TAG)
                    .commit()
        })
    }
}