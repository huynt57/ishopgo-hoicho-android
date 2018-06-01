package ishopgo.com.exhibition.ui.survey

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class SurveyActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return SurveyFragmentActionBar.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent?.extras ?: Bundle()
    }
}