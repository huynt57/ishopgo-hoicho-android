package ishopgo.com.exhibition.ui.main.stamp.nostamp.add

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.main.stamp.nostamp.NoStampViewModel
import ishopgo.com.exhibition.ui.main.stamp.nostamp.assign.NoStampSearchProductFragment

class NoStampAddActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return NoStampAddFragmentActionBar.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }


    private lateinit var viewModel: NoStampViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(NoStampViewModel::class.java)
        viewModel.openSearchProduct.observe(this, Observer { p ->
            p?.let {
                val params = Bundle()
                params.putLong(Const.TransferKey.EXTRA_ID, it)
                params.putLong(Const.TransferKey.EXTRA_STAMP_COUNT, it)
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .add(R.id.fragment_container, NoStampSearchProductFragment.newInstance(params))
                        .addToBackStack(NoStampSearchProductFragment.TAG)
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