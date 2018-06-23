package ishopgo.com.exhibition.ui.community.comment

import android.annotation.SuppressLint
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
import ishopgo.com.exhibition.model.community.CommunityComment
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.community.CommunityViewModel
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
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
    private var adapterImages = ComposingPostMediaAdapter()
    private var adapter = CommunityCommentAdapter()
    private lateinit var scroller: LinearSmoothScroller
    private lateinit var viewModel: CommunityViewModel
    private var post_id: Long = 0
    private var last_id: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comment_community, container, false)
    }


    companion object {
        const val TAG = "CommunityShareFragmentActionBar"

        fun newInstance(params: Bundle): CommunityCommentFragment {
            val fragment = CommunityCommentFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        post_id = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
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
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.adapter = adapter
        view_recyclerview.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
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

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<CommunityComment> {
            override fun click(position: Int, data: CommunityComment, code: Int) {
                val intent = Intent(view.context, MemberProfileActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                startActivity(intent)
            }
        }

        swipe.setOnRefreshListener(this)
        setupImageRecycleview()
    }

    private fun setupImageRecycleview() {
        context?.let {
            rv_comment_community_image.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_comment_community_image.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_comment_community_image.adapter = adapterImages
            adapterImages.listener = object : ClickableAdapter.BaseAdapterAction<PostMedia> {
                override fun click(position: Int, data: PostMedia, code: Int) {
                    postMedias.remove(data)
                    if (postMedias.isEmpty()) rv_comment_community_image.visibility = View.GONE
                    adapterImages.replaceAll(postMedias)
                }
            }
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    @SuppressLint("SetTextI18n")
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
                rv_comment_community_image.adapter = adapterImages
                rv_comment_community_image.visibility = View.GONE

                view_recyclerview.post {
                    scroller.targetPosition = 0
                    view_recyclerview.layoutManager.startSmoothScroll(scroller)
                }
            }
        })

        viewModel.loadCommentSusscess.observe(this, Observer { p ->
            p?.let {
                if (it.isNotEmpty()) {
                    last_id = it[it.size - 1].id
                }

                if (reloadData) {
                    if (it.isEmpty()) {
                        view_empty_result_notice.visibility = View.VISIBLE
                        view_empty_result_notice.text = "Nội dung trống"
                    } else view_empty_result_notice.visibility = View.GONE

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
        viewModel.loadCommentCommunity(post_id, 0, loadMore)
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
}