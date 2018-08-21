package ishopgo.com.exhibition.ui.main.product.detail.description

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Description
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asHtml
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.android.synthetic.main.fragment_product_full_detail.*

class DescriptionFragment : BaseActionBarFragment() {
    companion object {
        fun newInstance(params: Bundle): DescriptionFragment {
            val fragment = DescriptionFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private lateinit var data: Description

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_post_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json: String = arguments?.getString(Const.TransferKey.EXTRA_JSON) ?: ""
        data = Toolbox.gson.fromJson(json, Description::class.java)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()
        tv_category.visibility = View.GONE
        tv_post_time.visibility = View.GONE
        tv_post_title.text = "<b>Tiêu đề: ${data.title ?: ""}</b>".asHtml()

        if (!TextUtils.isEmpty(data.description ?: "")) {
            val fullHtml = String.format(
                    "<html><head><meta name=\"viewport\"/><style>%s</style></head><body>%s</body></html>",
                    Const.webViewCSS,
                    data.description ?: ""
            )
            view_webview.loadData(fullHtml, "text/html; charset=UTF-8", null)
        }

        view_webview.settings.javaScriptEnabled = true
        view_webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                } else if (newProgress < 100) {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                }
            }
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chi tiết mô tả")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }
    }
}