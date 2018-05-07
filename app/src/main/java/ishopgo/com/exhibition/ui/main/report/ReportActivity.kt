package ishopgo.com.exhibition.ui.main.report

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

/**
 * Created by xuanhong on 5/7/18. HappyCoding!
 */
class ReportActivity : BaseSingleFragmentActivity() {

    override fun createFragment(startupOption: Bundle): Fragment {
        return ReportFragmentActionBar.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent?.extras ?: Bundle()
    }

}