package ishopgo.com.exhibition.ui.main.salepointdetail

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.salepointdetail.qrcode.QrCodeSalePointFragment

class SalePointDetailActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return SalePointDetailFragmentActionBar.newInstance(startupOption)
    }

    private lateinit var viewModel: SalePointShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = obtainViewModel(SalePointShareViewModel::class.java)
        viewModel.showSalePointQRCode.observe(this, Observer {
            val extra = Bundle()
            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                    .add(R.id.fragment_container, QrCodeSalePointFragment.newInstance(extra))
                    .addToBackStack(QrCodeSalePointFragment.TAG)
                    .commit()
        })
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }
}