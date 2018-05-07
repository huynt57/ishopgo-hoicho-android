package ishopgo.com.exhibition.ui.main.product.detail.comment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ProductCommentsRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.Toolbox
import kotlinx.android.synthetic.main.fragment_comment_community.*

/**
 * Created by hoangnh on 5/4/2018.
 */
class CommentProductFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        reloadData = true
        firstLoad()
    }

    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private lateinit var adapterImages: ComposingPostMediaAdapter
    private var adapter = ProductCommentAdapter()
    private lateinit var scroller: LinearSmoothScroller
    private lateinit var viewModel: ProductCommentViewModel
    private var productId: Long = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comment_product, container, false)
    }

    companion object {
        fun newInstance(params: Bundle): CommentProductFragment {
            val f = CommentProductFragment()
            f.arguments = params

            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (UserDataManager.currentUserId > 0) {
            linearLayout.visibility = View.VISIBLE
        } else linearLayout.visibility = View.GONE

        scroller = object : LinearSmoothScroller(context) {

            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_comment_community.layoutManager = layoutManager
        rv_comment_community.adapter = adapter
        rv_comment_community.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore()
            }
        })

        img_comment_gallery.setOnClickListener { launchPickPhotoIntent() }

        img_comment_sent.setOnClickListener {
            if (checkRequireFields(edt_comment.text.toString())) {
                showProgressDialog()
                viewModel.postCommentProduct(productId, edt_comment.text.toString(), 0, postMedias)
            }
        }

        swipe.setOnRefreshListener(this)
        rv_comment_community.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        rv_comment_community.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
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
        viewModel = obtainViewModel(ProductCommentViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.postCommentSuccess.observe(this, Observer { c ->
            c?.let {
                firstLoad()

                hideProgressDialog()
                edt_comment.setText("")
                postMedias.clear()
                adapterImages = ComposingPostMediaAdapter(postMedias)
                rv_comment_community_image.adapter = adapterImages
                rv_comment_community_image.visibility = View.GONE

                rv_comment_community.post {
                    scroller.targetPosition = 0
                    rv_comment_community.layoutManager.startSmoothScroll(scroller)
                }
            }
        })

        viewModel.dataReturned.observe(this, Observer { p ->
            p?.let {
                if (reloadData) {
                    adapter.replaceAll(it)
                    hideProgressDialog()
                } else {
                    adapter.addAll(it)
                }
                swipe.isRefreshing = false
            }
        })

        reloadData = true
        swipe.isRefreshing = true
        firstLoad()
    }

    fun firstLoad() {
        reloadData = true
        val loadMore = ProductCommentsRequest()
        loadMore.lastId = -1L
        loadMore.parentId = -1L
        loadMore.productId = productId
        loadMore.limit = Const.PAGE_LIMIT
        viewModel.loadData(loadMore)
        rv_comment_community.scheduleLayoutAnimation()

    }

    fun loadMore() {
        reloadData = false
        val loadMore = ProductCommentsRequest()
        val item = adapter.getItem(adapter.itemCount - 1)
        if (item is IdentityData) {
            loadMore.lastId = item.id
            loadMore.parentId = -1L
            loadMore.productId = productId
            loadMore.limit = Const.PAGE_LIMIT
            viewModel.loadData(loadMore)
        }
    }

    private fun checkRequireFields(content: String): Boolean {
        if (content.trim().isEmpty()) {
            toast("Nội dung quá ngắn hoặc chưa đầy đủ")
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (data.clipData == null) {
                if (Toolbox.exceedSize(context, data.data, (2 * 1024 * 1024).toLong())) {
                    toast("Chỉ đính kèm được ảnh có dung lượng dưới 2 MB. Hãy chọn file khác.")
                    return
                }
                val postMedia = PostMedia()
                postMedia.uri = data.data
                postMedias.add(postMedia)


            } else {
                for (i in 0 until data.clipData.itemCount) {
                    if (Toolbox.exceedSize(context, data.clipData.getItemAt(i).uri, (2 * 1024 * 1024).toLong())) {
                        toast("Chỉ đính kèm được ảnh có dung lượng dưới 2 MB. Hãy chọn file khác.")
                        return
                    }
                    val postMedia = PostMedia()
                    postMedia.uri = data.clipData.getItemAt(i).uri
                    postMedias.add(postMedia)
                }
            }
            adapterImages = ComposingPostMediaAdapter(postMedias)
            adapterImages.notifyItemInserted(postMedias.size - 1)
            rv_comment_community_image.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_comment_community_image.adapter = adapterImages

            rv_comment_community_image.visibility = View.VISIBLE
        }
    }
}