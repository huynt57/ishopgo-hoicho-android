package ishopgo.com.exhibition.ui.main.WebViewer

import android.os.Bundle
import android.support.v4.app.Fragment
import ishopgo.com.exhibition.ui.base.BaseSingleFragmentActivity

class WebViewActivity : BaseSingleFragmentActivity() {
    override fun createFragment(startupOption: Bundle): Fragment {
        val html = intent.getStringExtra(EXTRA_HTML)
        return WebViewFragmentActionBar.newInstance(html,startupOption)
    }

    companion object {
        val EXTRA_HTML = "html"
    }
}