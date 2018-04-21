package ishopgo.com.exhibition.ui.main.product.detail

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity
import ishopgo.com.exhibition.ui.extensions.transact

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class ProductDetailActivity : BaseSingleFragmentActivity() {
    companion object {
        private val TAG = "ProductDetailActivity"
    }

    override fun createFragment(startupOption: Bundle): Fragment {
        return ProductDetailFragment.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent: intent = [${intent}]")

        supportFragmentManager.transact {
            setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
            replace(R.id.fragment_container, ProductDetailFragment.newInstance(intent?.extras
                    ?: Bundle()))
            addToBackStack(null)
        }
    }


}