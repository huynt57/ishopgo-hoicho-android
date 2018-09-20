package ishopgo.com.exhibition.ui.main.stamp.buystamp.add

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class BuyStampAddActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return BuyStampAddFragmentActionBar.newInstance(startupOption)
    }
}