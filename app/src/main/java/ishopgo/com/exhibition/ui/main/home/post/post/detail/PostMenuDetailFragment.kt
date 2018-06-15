package ishopgo.com.exhibition.ui.main.home.post.post.detail

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
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.home.post.post.PostMenuViewModel
import ishopgo.com.exhibition.ui.main.postmanager.detail.PostManagerDetailConverter
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.android.synthetic.main.fragment_product_full_detail.*

class PostMenuDetailFragment : BaseFragment() {
    private lateinit var data: PostObject
    private lateinit var viewModel: PostMenuViewModel

    companion object {
        fun newInstance(params: Bundle): PostMenuDetailFragment {
            val fragment = PostMenuDetailFragment()
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
        data = Toolbox.gson.fromJson(json, PostObject::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val convert = PostManagerDetailConverter().convert(data)
        tv_post_title.text = convert.provideTitle()
        tv_post_time.text = convert.provideInfo()
        tv_category.text = convert.provideCategory()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(PostMenuViewModel::class.java, false)
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