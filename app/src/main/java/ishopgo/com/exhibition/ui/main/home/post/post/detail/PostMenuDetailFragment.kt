package ishopgo.com.exhibition.ui.main.home.post.post.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.afollestad.materialdialogs.MaterialDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.post.PostContent
import ishopgo.com.exhibition.model.post.PostObject
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.home.post.post.PostMenuViewModel
import ishopgo.com.exhibition.ui.main.postmanager.detail.PostManagerDetailConverter
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.android.synthetic.main.fragment_product_full_detail.*

class PostMenuDetailFragment : BaseFragment() {
    private lateinit var data: PostObject
    private lateinit var viewModel: PostMenuViewModel
    private var postContent: PostContent? = null

    companion object {
        const val TAG = "PostMenuDetailFragment"
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
                postContent = it
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
                        if (progressBar != null)
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

        data.id.let { viewModel.getPostContent(it) }
    }

    fun sharePost() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_community_share, false)
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()
            val tv_share_facebook = dialog.findViewById(R.id.tv_share_facebook) as VectorSupportTextView
            tv_share_facebook.setOnClickListener {
                postContent?.let { it1 -> shareFacebook(it1) }
            }
            val tv_share_zalo = dialog.findViewById(R.id.tv_share_zalo) as VectorSupportTextView
            tv_share_zalo.setOnClickListener {
                postContent?.let { it1 -> shareApp(it1) }
            }
            dialog.show()
        }
    }

    private var callbackManager: CallbackManager? = null

    private fun shareFacebook(postContent: PostContent) {
        callbackManager = CallbackManager.Factory.create()
        val shareDialog = ShareDialog(this)

        shareDialog.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
            override fun onSuccess(result: Sharer.Result?) {
            }

            override fun onCancel() {
                toast("Chia sẻ bị huỷ bỏ")
            }

            override fun onError(error: FacebookException?) {
                toast(error.toString())
            }
        })

        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            val urlToShare = postContent.linkShare.toString()
//            val urlToShare = String.format("%s://%s/tin-tuc", getString(R.string.app_protocol), getString(R.string.app_host))
            val shareContent = ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(urlToShare))
                    .build()

            shareDialog.show(shareContent)
        }
    }

    private fun shareApp(postContent: PostContent) {
        val urlToShare = postContent.linkShare.toString()
//        val urlToShare = String.format("%s://%s/tin-tuc", getString(R.string.app_protocol), getString(R.string.app_host))
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, urlToShare)
        startActivity(shareIntent)
    }
}