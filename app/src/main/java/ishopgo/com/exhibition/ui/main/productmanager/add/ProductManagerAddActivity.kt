package ishopgo.com.exhibition.ui.main.productmanager.add

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.productmanager.search_product.FilterSearchFragment
import ishopgo.com.exhibition.ui.main.productmanager.search_product.SearchProductFragmentActionBar
import ishopgo.com.exhibition.ui.main.productmanager.search_product.SearchProductManagerViewModel

class ProductManagerAddActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return ProductManagerAddFragmentActionBar.newInstance(startupOption)
    }

    private lateinit var viewModel: SearchProductManagerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(SearchProductManagerViewModel::class.java)
        viewModel.showSearchSp.observe(this, Observer { p ->
            p?.let {
                val params = Bundle()
                params.putInt(Const.TransferKey.EXTRA_REQUIRE, it)
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .add(R.id.fragment_container, SearchProductFragmentActionBar.newInstance(params))
                        .addToBackStack(SearchProductFragmentActionBar.TAG)
                        .commit()
            }
        })

        viewModel.showFilterSp.observe(this, Observer { p ->
            p?.let {
                val params = Bundle()
                params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .add(R.id.fragment_container, FilterSearchFragment.newInstance(params))
                        .addToBackStack(FilterSearchFragment.TAG)
                        .commit()
            }
        })
    }

    override fun onBackPressed() {
        if (currentFragment is BackpressConsumable && (currentFragment as BackpressConsumable).onBackPressConsumed())
            return
        else {
            super.onBackPressed()
        }
    }
}