package ishopgo.com.exhibition.ui.main.generalmanager.news

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class PostManagerActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return PostManagerFragmentActionBar.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }
}