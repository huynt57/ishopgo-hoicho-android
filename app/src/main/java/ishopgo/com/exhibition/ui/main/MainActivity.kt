package ishopgo.com.exhibition.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MainActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return MainFragment()
    }

    override fun onBackPressed() {
        if (currentFragment is BackpressConsumable) {
            val isConsumed = (currentFragment as BackpressConsumable).onBackPressConsumed()
            if (isConsumed)
                return
            else return super.onBackPressed()
        } else
            super.onBackPressed()
    }

}