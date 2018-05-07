package ishopgo.com.exhibition.ui.main.product.detail.fulldetail

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_product_full_detail.*

/**
 * Created by xuanhong on 5/7/18. HappyCoding!
 */
class WebViewFragment : BaseFragment() {

    companion object {

        fun newInstance(params: Bundle): WebViewFragment {
            val fragment = WebViewFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private var json: String = ""
    private var url: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_full_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        json = arguments?.getString(Const.TransferKey.EXTRA_JSON) ?: ""
        url = arguments?.getString(Const.TransferKey.EXTRA_URL) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!TextUtils.isEmpty(json)) {
            val fullHtml = String.format(
                    "<html><head><meta name=\"viewport\"/><style>%s</style></head><body>%s</body></html>",
                    Const.webViewCSS,
                    json
            )
            view_webview.loadData(fullHtml, "text/html; charset=UTF-8", null)
        } else {
            view_webview.loadUrl(url)
        }
        view_webview.settings.javaScriptEnabled = true
    }

}