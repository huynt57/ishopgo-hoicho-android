package ishopgo.com.exhibition.ui.main.postmanager.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.post.PostObject
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.postmanager.PostViewModel
import ishopgo.com.exhibition.ui.widget.Toolbox
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.android.synthetic.main.fragment_product_full_detail.*

class PostManagerDetailFragment : BaseFragment() {
    private var data: PostObject? = null
    private lateinit var viewModel: PostViewModel

    companion object {
        fun newInstance(params: Bundle): PostManagerDetailFragment {
            val fragment = PostManagerDetailFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json: String = arguments?.getString(Const.TransferKey.EXTRA_JSON) ?: ""
        data = Toolbox.getDefaultGson().fromJson(json, PostObject::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_post_title.text = data?.provideTitle() ?: ""
        tv_post_time.text = data?.provideTime() ?: ""
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(PostViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.getContentSusscess.observe(this, Observer { p ->
            p.let {
                if (!TextUtils.isEmpty(it?.content)) {
                    val fullHtml = String.format(
                            "<html><head><meta name=\"viewport\"/><style>%s</style></head><body>%s</body></html>",
                            Const.webViewCSS,
                            it?.content
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
        })

        data?.id?.let { viewModel.getPostContent(it) }
    }
}