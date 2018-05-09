package ishopgo.com.exhibition.ui.main.WebViewer

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_full_detail.*
import org.apache.commons.io.IOUtils
import java.io.IOException

class WebViewFragment : BaseFragment() {
    private var html = ""

    companion object {
        fun newInstance(html: String, params: Bundle): WebViewFragment {
            val fragment = WebViewFragment()
            fragment.arguments = params
            fragment.html = html

            return fragment
        }
        internal var CSS: String? = null

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_full_detail, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (CSS == null) {
            try {
                CSS = IOUtils.toString(context?.assets?.open("WebViewStyle.css"), "UTF-8")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        if (!TextUtils.isEmpty(html)) {
            val fullHtml = String.format(
                    "<html><head><meta name=\"viewport\"/><style>%s</style></head><body>%s</body></html>",
                    CSS,
                    html
            )
            webView.loadData(fullHtml, "text/html; charset=UTF-8", null)
        }
        webView.settings.javaScriptEnabled = true
    }
}