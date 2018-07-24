package ishopgo.com.exhibition.ui.community.comment.child

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreCommunityRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.community.CommunityComment
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.community.CommunityViewModel
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_community_result_detail.*

class CommunityCommentChildFragment : BaseActionBarFragment(), SwipeRefreshLayout.OnRefreshListener, BackpressConsumable {
    override fun onBackPressConsumed(): Boolean {
        return childFragmentManager.popBackStackImmediate()
    }

    override fun onRefresh() {
        reloadData = true
        firstLoad()
    }

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_community_result_detail
    }

    companion object {
        const val TAG = "CommunityCommentChildFragment"

        fun newInstance(params: Bundle): CommunityCommentChildFragment {
            val fragment = CommunityCommentChildFragment()
            fragment.arguments = params

            return fragment
        }

        const val COMMUNITY_REPLY = 0
        const val COMMUNITY_REPLY_CHILD = 1
    }

    private lateinit var communityComment: CommunityComment
    private var adapter = CommentChildAdapter()
    private lateinit var viewModel: CommunityViewModel
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private var adapterImages = ComposingPostMediaAdapter()
    private var postId: Long = -1L
    private var lastId: Long = -1L
    private var parentId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        communityComment = Toolbox.gson.fromJson(json, CommunityComment::class.java)
        postId = communityComment.postId ?: -1L
        parentId = communityComment.id
    }

    fun firstLoad() {
        reloadData = true
        val firstLoad = LoadMoreCommunityRequest()
        firstLoad.post_id = postId
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.last_id = 0
        firstLoad.parent_id = parentId
        viewModel.loadCommentCommunity(firstLoad)
    }

    fun loadMore() {
        reloadData = false
        val loadMore = LoadMoreCommunityRequest()
        loadMore.post_id = postId
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.last_id = lastId
        loadMore.parent_id = parentId
        viewModel.loadCommentCommunity(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app_bar_layout.visibility = View.GONE

        if (UserDataManager.currentUserId > 0) {
            linearLayout.visibility = View.VISIBLE
        } else linearLayout.visibility = View.GONE

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.adapter = adapter
        view_recyclerview.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore()
            }
        })

        tv_reply.setOnClickListener { tv_reply.visibility = View.GONE }

        img_comment_gallery.setOnClickListener { launchPickPhotoIntent() }

        img_comment_sent.setOnClickListener {
            if (isRequiredFieldsValid(edt_comment.text.toString())) {
                showProgressDialog()
                viewModel.postCommentCommunity(postId, edt_comment.text.toString(), postMedias, parentId)
            }
        }

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<CommunityComment> {
            @SuppressLint("SetTextI18n")
            override fun click(position: Int, data: CommunityComment, code: Int) {
                when (code) {
                    COMMUNITY_REPLY -> {
                        tv_reply.visibility = View.VISIBLE
                        tv_reply.text = "Trả lời bình luận của ${data.accountName}"
                    }

                    COMMUNITY_REPLY_CHILD -> {
                        tv_reply.visibility = View.VISIBLE
                        tv_reply.text = "Trả lời bình luận của ${data.accountName}"
                    }

                    else -> {
                        val intent = Intent(view.context, MemberProfileActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                        startActivity(intent)
                    }
                }
            }
        }

        setupImageRecycleview()
        setupToolbars()
        swipe.setOnRefreshListener(this)
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Chi tiết bình luận")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.onBackPressed() }
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
                edt_comment.hideKeyboard()
                firstLoad()
                tv_reply.visibility = View.GONE
                hideProgressDialog()
                edt_comment.setText("")
                postMedias.clear()
                adapterImages.replaceAll(postMedias)
                rv_comment_community_image.adapter = adapterImages
                rv_comment_community_image.visibility = View.GONE
            }
        })

        viewModel.loadCommentSusscess.observe(this, Observer { p ->
            p?.let {
                if (it.isNotEmpty()) {
                    lastId = it[it.size - 1].id
                }

                if (reloadData) {
                    hideProgressDialog()

                    adapter.replaceAll(it)
                    adapter.addData(0, communityComment)
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

    private fun isRequiredFieldsValid(content: String): Boolean {
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