package ishopgo.com.exhibition.ui.main.stamp.buystamp.update

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.stamp.buystamp.update.history.BuyStampHistoryFragmentActionBar

class BuyStampUpdatedActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return BuyStampUpdatedFragmentActionBar.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }

    private lateinit var viewModel: ShareStampOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(ShareStampOrderViewModel::class.java)
        viewModel.showStampOrderHistory.observe(this, Observer { p ->
            p?.let {
                val params = Bundle()
                params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .add(R.id.fragment_container, BuyStampHistoryFragmentActionBar.newInstance(params))
                        .addToBackStack(BuyStampHistoryFragmentActionBar.TAG)
                        .commit()
            }
        })
    }
}