package ishopgo.com.exhibition.ui.main.visitors

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class VisitorsActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return VisitorsFragmentActionBar.newInstance(startupOption)
    }
}