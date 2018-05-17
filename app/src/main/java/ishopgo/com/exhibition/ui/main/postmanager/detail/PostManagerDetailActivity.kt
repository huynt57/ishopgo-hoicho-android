package ishopgo.com.exhibition.ui.main.postmanager.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class PostManagerDetailActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        return PostManagerDetailFragmentActionBar.newInstance(startupOption)
    }

    override fun startupOptions(): Bundle {
        return intent.extras ?: Bundle()
    }
}