package ishopgo.com.exhibition.ui.main.product.detail.fulldetail

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class FullDetailActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return FullDetailFragment.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent?.extras ?: Bundle()
    }


}