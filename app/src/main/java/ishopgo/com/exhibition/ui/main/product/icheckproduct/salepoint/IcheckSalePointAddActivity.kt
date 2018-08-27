package ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductViewModel

class IcheckSalePointAddActivity : BaseSingleFragmentActivity() {
    private lateinit var viewModel: IcheckProductViewModel

    override fun createFragment(startupOption: Bundle): Fragment {
        return IcheckSalePointAddFragment.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = obtainViewModel(IcheckProductViewModel::class.java)
        viewModel.openIcheckCategory.observe(this, Observer {
            val params = Bundle()
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                    .add(R.id.fragment_container, IcheckCategoryFragment.newInstance(params))
                    .addToBackStack(IcheckCategoryFragment.TAG)
                    .commit()
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