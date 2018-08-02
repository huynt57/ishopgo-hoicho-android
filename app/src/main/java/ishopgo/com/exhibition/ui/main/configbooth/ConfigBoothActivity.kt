package ishopgo.com.exhibition.ui.main.configbooth

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.AttributeSet
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by hoangnh on 5/5/2018.
 */
class ConfigBoothActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return ConfigBoothFragmentActionBar()
    }

    private lateinit var viewModel: ShareBoothViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = obtainViewModel(ShareBoothViewModel::class.java)
        viewModel.showBoothRelateFloat.observe(this, Observer {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                    .add(R.id.fragment_container, BoothRelateFragment.newInstance(Bundle()))
                    .addToBackStack(BoothRelateFragment.TAG)
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