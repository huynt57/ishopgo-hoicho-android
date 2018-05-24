package ishopgo.com.exhibition.ui.main.home.search.community.detail

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreCommunityRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.community.Community
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.community.CommunityProvider
import ishopgo.com.exhibition.ui.community.CommunityViewModel
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.community.comment.CommunityCommentAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.login.LoginSelectOptionActivity
import ishopgo.com.exhibition.ui.main.home.search.community.SearchCommunityAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.fragment_comment_community.*
import kotlinx.android.synthetic.main.fragment_community_result_detail.*

class CommunityResultDetailFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    private var adapterImages = ComposingPostMediaAdapter()
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private var adapterComment = CommunityCommentAdapter()
    private var adapterParent = SearchCommunityAdapter()
    private lateinit var viewModel: CommunityViewModel
    private lateinit var data: Community
    private var last_id: Long = 0

    override fun onRefresh() {
        reloadData = true
        firstLoad()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_community_result_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, Community::class.java)
        val parent = mutableListOf<Community>()
        parent.add(data)
        adapterParent.replaceAll(parent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleview()
        img_comment_gallery.setOnClickListener { launchPickPhotoIntent() }

        img_comment_sent.setOnClickListener {
            if (checkRequireFields(edt_comment.text.toString())) {
                showProgressDialog()
                viewModel.postCommentCommunity(data.id, edt_comment.text.toString(), postMedias)
            }
        }

        swipe.setOnRefreshListener(this)
    }

    fun firstLoad() {
        reloadData = true
        val firstLoad = LoadMoreCommunityRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.last_id = 0
        viewModel.loadCommentCommunity(data.id, 0, firstLoad)
    }

    fun loadMore() {
        reloadData = false
        val loadMore = LoadMoreCommunityRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.last_id = last_id
        viewModel.loadData(loadMore)
    }

    private fun checkRequireFields(content: String): Boolean {
        if (content.trim().isEmpty()) {
            toast("Nội dung quá ngắn hoặc chưa đầy đủ")
            return false
        }
        return true
    }

    private fun setupRecycleview() {
        rv_community_parent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_community_parent.adapter = adapterParent
        if (adapterParent is ClickableAdapter<CommunityProvider>) {
            (adapterParent as ClickableAdapter<CommunityProvider>).listener = object : ClickableAdapter.BaseAdapterAction<CommunityProvider> {
                override fun click(position: Int, data: CommunityProvider, code: Int) {
                    when (code) {
                        COMMUNITY_LIKE_CLICK -> {
                            if (data is Community) {
                                if (viewModel is CommunityViewModel) viewModel.postCommunityLike(data.id)
                            }
                        }

                        COMMUNITY_SHARE_NUMBER_CLICK -> {
                            context?.let {
                                val dialog = MaterialDialog.Builder(it)
                                        .customView(R.layout.dialog_community_share, false)
                                        .autoDismiss(false)
                                        .canceledOnTouchOutside(true)
                                        .build()
                                val tv_share_facebook = dialog.findViewById(R.id.tv_share_facebook) as VectorSupportTextView
                                tv_share_facebook.setOnClickListener {
                                    shareFacebook(data)
                                }
                                val tv_share_zalo = dialog.findViewById(R.id.tv_share_zalo) as VectorSupportTextView
                                tv_share_zalo.setOnClickListener {
                                    shareApp(data)
                                }
                                dialog.show()
                            }
                        }

                        COMMUNITY_SHARE_PRODUCT_CLICK -> {
                            context?.let {
                                val dialog = MaterialDialog.Builder(it)
                                        .customView(R.layout.dialog_community_share, false)
                                        .autoDismiss(false)
                                        .canceledOnTouchOutside(true)
                                        .build()
                                val tv_share_facebook = dialog.findViewById(R.id.tv_share_facebook) as VectorSupportTextView
                                tv_share_facebook.setOnClickListener {
                                    shareFacebook(data)
                                }
                                val tv_share_zalo = dialog.findViewById(R.id.tv_share_zalo) as VectorSupportTextView
                                tv_share_zalo.setOnClickListener {
                                    shareApp(data)
                                }
                                dialog.show()
                            }
                        }

                        COMMUNITY_PRODUCT_CLICK -> {
                            if (data is Community) {
                                val productId = data.product?.id ?: -1L
                                if (productId != -1L) {
                                    context?.let {
                                        val intent = Intent(it, ProductDetailActivity::class.java)
                                        intent.putExtra(Const.TransferKey.EXTRA_ID, productId)
                                        startActivity(intent)
                                    }
                                }
                            }
                        }
                        COMMUNITY_IMAGE_CLICK -> {
                            val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, data.provideListImage().toTypedArray())
                            startActivity(intent)
                        }
                    }
                }
            }
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_comment_community.layoutManager = layoutManager
        rv_comment_community.adapter = adapterComment
        rv_comment_community.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore()
            }
        })

        rv_comment_community_image.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_comment_community_image.adapter = adapterImages
        adapterImages.listener = object : ClickableAdapter.BaseAdapterAction<PostMedia> {
            override fun click(position: Int, data: PostMedia, code: Int) {
                postMedias.remove(data)
                if (postMedias.isEmpty()) rv_comment_community_image.visibility = View.GONE
                adapterImages.replaceAll(postMedias)
            }
        }
    }

    private fun shareFacebook(data: CommunityProvider) {
        val urlToShare = data.provideProduct()?.providerLink()
        var intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"

        intent.putExtra(Intent.EXTRA_TEXT, urlToShare)

        var facebookAppFound = false
        val matches = context!!.packageManager.queryIntentActivities(intent, 0)
        for (info in matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.`package` = info.activityInfo.packageName
                facebookAppFound = true
                break
            }
        }

        if (!facebookAppFound) {
            val sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=$urlToShare"
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
        }

        startActivity(intent)
    }

    private fun shareApp(data: CommunityProvider) {
        val urlToShare = data.provideProduct()?.providerLink()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, urlToShare)
        startActivity(shareIntent)
    }

    private fun showDiglogLogin() {
        context?.let {
            val builder = MaterialDialog.Builder(it)
            builder.title("Thông báo")
                    .content("Bạn cần đăng nhập để sử dụng tính năng này!")
                    .positiveText("Đăng nhập")
                    .positiveColor(Color.parseColor("#00c853"))
                    .onPositive { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(context, LoginSelectOptionActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
                        startActivity(intent)
                        activity?.finish()
                    }
                    .negativeText("Bỏ qua")
                    .negativeColor(Color.parseColor("#00c853"))
                    .show()
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(CommunityViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.postCommentSusscess.observe(this, Observer { c ->
            c?.let {
                firstLoad()

                hideProgressDialog()
                edt_comment.setText("")
                postMedias.clear()
                adapterImages.replaceAll(postMedias)
                rv_comment_community_image.visibility = View.GONE
            }
        })

        viewModel.loadCommentSusscess.observe(this, Observer { p ->
            p?.let {
                if (it.isNotEmpty()) {
                    last_id = it[it.size - 1].id
                }

                if (reloadData) {
                    adapterComment.replaceAll(it)
                    hideProgressDialog()
                } else {
                    adapterComment.addAll(it)
                }
                swipe.isRefreshing = false
            }
        })
        reloadData = true
        swipe.isRefreshing = true
        firstLoad()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (data.clipData == null) {
                if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                    toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                    return
                }
                val postMedia = PostMedia()
                postMedia.uri = data.data
                postMedias.add(postMedia)


            } else {
                for (i in 0 until data.clipData.itemCount) {
                    if (Toolbox.exceedSize(context!!, data.clipData.getItemAt(i).uri, (5 * 1024 * 1024).toLong())) {
                        toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                        return
                    }
                    val postMedia = PostMedia()
                    postMedia.uri = data.clipData.getItemAt(i).uri
                    postMedias.add(postMedia)
                }
            }

            adapterImages.replaceAll(postMedias)
            rv_comment_community_image.visibility = View.VISIBLE
        }
    }

    companion object {

        fun newInstance(params: Bundle): CommunityResultDetailFragment {
            val fragment = CommunityResultDetailFragment()
            fragment.arguments = params

            return fragment
        }

        const val COMMUNITY_LIKE_CLICK = 2
        const val COMMUNITY_SHARE_NUMBER_CLICK = 4
        const val COMMUNITY_SHARE_PRODUCT_CLICK = 5
        const val COMMUNITY_PRODUCT_CLICK = 6
        const val COMMUNITY_IMAGE_CLICK = 7
    }
}