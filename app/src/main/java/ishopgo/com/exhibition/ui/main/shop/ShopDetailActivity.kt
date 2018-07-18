package ishopgo.com.exhibition.ui.main.shop

import android.content.Intent
import android.os.Bundle
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseNavigationActivity

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ShopDetailActivity : BaseNavigationActivity() {
    override fun navigationRes(): Int {
        return R.navigation.nav_booth_detail
    }

    override fun startArguments(): Bundle {
        val shopId = intent?.getLongExtra(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
        val bundle = Bundle()
        bundle.putLong(Const.TransferKey.EXTRA_ID, shopId)
        return bundle
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_shopDetailFragmentActionBar_self, intent?.extras ?: Bundle())
    }

}