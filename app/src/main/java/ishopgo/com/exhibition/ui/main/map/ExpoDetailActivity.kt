package ishopgo.com.exhibition.ui.main.map

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.map.choosebooth.ChooseBoothFragment
import ishopgo.com.exhibition.ui.main.map.qrcode.ExpoQrCodeFragment
import ishopgo.com.exhibition.ui.main.map.searchbooth.SearchBoothFragment
import ishopgo.com.exhibition.ui.main.registerbooth.RegisterBoothFragment
import ishopgo.com.exhibition.ui.main.registerbooth.RegisterBoothFragmentActionBar
import ishopgo.com.exhibition.ui.main.ticket.detail.TicketDetailFragmentActionBar

class ExpoDetailActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return ExpoDetailFragment2.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }

    private lateinit var viewModel: ExpoMapShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(ExpoMapShareViewModel::class.java)
        viewModel.showQrCodeFragment.observe(this, Observer { p ->
            p?.let {
                val params = Bundle()
                params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .add(R.id.fragment_container, ExpoQrCodeFragment.newInstance(params))
                        .addToBackStack(ExpoQrCodeFragment.TAG)
                        .commit()
            }
        })

        viewModel.showTicketDetailFragment.observe(this, Observer { p ->
            p?.let {
                val params = Bundle()
                params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(it))
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .add(R.id.fragment_container, TicketDetailFragmentActionBar.newInstance(params))
                        .addToBackStack(TicketDetailFragmentActionBar.TAG)
                        .commit()
            }
        })

        viewModel.showBoothSelectFragment.observe(this, Observer { p ->
            p?.let {
                val params = Bundle()
                params.putLong(Const.TransferKey.EXTRA_ID, it)
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .add(R.id.fragment_container, ChooseBoothFragment.newInstance(params))
                        .addToBackStack(ChooseBoothFragment.TAG)
                        .commit()
            }
        })

        viewModel.showSearchBoothFragment.observe(this, Observer { p ->
            p?.let {
                val params = Bundle()
                params.putLong(Const.TransferKey.EXTRA_ID, it)
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .add(R.id.fragment_container, SearchBoothFragment.newInstance(params))
                        .addToBackStack(SearchBoothFragment.TAG)
                        .commit()
            }
        })

        viewModel.showRegisterExpoFragment.observe(this, Observer { p ->
            p?.let {
                val params = Bundle()
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .add(R.id.fragment_container, RegisterBoothFragmentActionBar.newInstance(params))
                        .addToBackStack(RegisterBoothFragmentActionBar.TAG)
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

    //    override fun onSupportNavigateUp(): Boolean {
//        return Navigation.findNavController(this, R.id.nav_map_host_fragment).navigateUp()
//    }
}
