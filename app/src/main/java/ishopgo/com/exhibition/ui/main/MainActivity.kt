package ishopgo.com.exhibition.ui.main

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.widget.Toast
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MainActivity : BaseSingleFragmentActivity() {

    private var backpressCount = 1 // backpresscount = 2 will exit application
    private val resetBackpressRunable = object : Runnable {
        override fun run() {
            backpressCount = 1
        }
    }

    override fun createFragment(startupOption: Bundle): Fragment {
        return MainFragment()
    }

    override fun onBackPressed() {
        if (currentFragment is BackpressConsumable) {
            val isConsumed = (currentFragment as BackpressConsumable).onBackPressConsumed()
            if (isConsumed)
                return
            else return super.onBackPressed()
        } else {
            if (backpressCount == 2)
                super.onBackPressed()
            else {
                backpressCount++
                Handler().postDelayed(resetBackpressRunable, 1500)
                Toast.makeText(this, "Ấn back một lần nữa để thoát ứng dụng !", Toast.LENGTH_SHORT).show()
            }
        }
    }

}