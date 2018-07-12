package ishopgo.com.exhibition.ui.main.product.newest

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.filterproduct.FilterProductFragment
import ishopgo.com.exhibition.ui.filterproduct.FilterProductViewModel

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class NewestProductsActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return NewestProductsFragmentActionBar.newInstance(startupOption)
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
                    .add(R.id.fragment_container, FilterProductFragment.newInstance(extra))
                    .addToBackStack(FilterProductFragment.TAG)
                    .commit()
        })
    }

    override fun onBackPressed() {
        if (currentFragment is BackpressConsumable && (currentFragment as BackpressConsumable).onBackPressConsumed())
            return
        else
            super.onBackPressed()
    }
}