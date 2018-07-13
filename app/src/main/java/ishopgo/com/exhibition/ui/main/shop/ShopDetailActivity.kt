package ishopgo.com.exhibition.ui.main.shop

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.shop.qrcode.QrCodeShopFragment

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ShopDetailActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return ShopDetailFragmentActionBar.newInstance(startupOption)
    }

    private lateinit var viewModel: ShopDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = obtainViewModel(ShopDetailViewModel::class.java)
        viewModel.showShopDetailQRCode.observe(this, Observer {
            val extra = Bundle()
            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                    .add(R.id.fragment_container, QrCodeShopFragment.newInstance(extra))
                    .addToBackStack(QrCodeShopFragment.TAG)
                    .commit()
        })
    }

    override fun onBackPressed() {
        if (currentFragment is BackpressConsumable && (currentFragment as BackpressConsumable).onBackPressConsumed())
            return

        super.onBackPressed()
    }

    override fun startupOptions(): Bundle {
        return intent?.extras ?: Bundle()
    }

}