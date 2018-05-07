package ishopgo.com.exhibition.ui.community.comment

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
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreCommunityRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.community.CommunityViewModel
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.Toolbox
import kotlinx.android.synthetic.main.fragment_comment_community.*

/**
 * Created by hoangnh on 5/4/2018.
 */
class CommunityCommentFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        reloadData = true
        firstLoad()
    }

    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private lateinit var adapterImages: ComposingPostMediaAdapter
    private var adapter = CommunityCommentAdapter()
    private lateinit var scroller: LinearSmoothScroller
    private lateinit var viewModel: CommunityViewModel
    private var post_id: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comment_community, container, false)
    }

    private var last_id: Long = 0

    companion object {
        const val TAG = "CommunityShareFragmentActionBar"

        fun newInstance(post_id: Long): CommunityCommentFragment {
            val fragment = CommunityCommentFragment()
            fragment.post_id = post_id

            return fragment
        }
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
                viewModel.postCommentCommunity(post_id, edt_comment.text.toString(), postMedias)
            }
        }

        swipe.setOnRefreshListener(this)

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
                adapterImages = ComposingPostMediaAdapter(postMedias)
                rv_comment_community_image.adapter = adapterImages
                rv_comment_community_image.visibility = View.GONE

                rv_comment_community.post {
                    scroller.targetPosition = 0
                    rv_comment_community.layoutManager.startSmoothScroll(scroller)
                }
            }
        })

        viewModel.loadCommentSusscess.observe(this, Observer { p ->
            p?.let {
                if (it.isNotEmpty()) {
                    last_id = it[it.size - 1].id
                }

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
        val firstLoad = LoadMoreCommunityRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.last_id = 0
        viewModel.loadCommentCommunity(post_id, 0, firstLoad)
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