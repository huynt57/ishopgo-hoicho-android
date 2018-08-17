package ishopgo.com.exhibition.ui.main.stamp.buystamp

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class BuyStampActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return BuyStampFragmentActionBar.newInstance(startupOption)
    }
}