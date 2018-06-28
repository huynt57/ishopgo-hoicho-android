package ishopgo.com.exhibition.ui.community.comment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreCommunityRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.community.Community
import ishopgo.com.exhibition.model.community.CommunityComment
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.community.CommunityViewModel
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.home.search.community.detail.CommunityParentAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_community_result_detail.*
import java.io.IOException
import java.net.URL

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
    private var adapterParent = CommunityParentAdapter()
    private lateinit var viewModel: CommunityViewModel
    private lateinit var childViewModel: CommentViewModel
    private lateinit var data: Community
    private var post_id: Long = -1L
    private var last_id: Long = -1L
    private var parentId: Long = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_community_result_detail, container, false)
    }


    companion object {
        const val TAG = "CommunityShareFragmentActionBar"

        fun newInstance(params: Bundle): CommunityCommentFragment {
            val fragment = CommunityCommentFragment()
            fragment.arguments = params

            return fragment
        }

        const val COMMUNITY_LIKE_CLICK = 2
        const val COMMUNITY_SHARE_NUMBER_CLICK = 4
        const val COMMUNITY_SHARE_PRODUCT_CLICK = 5
        const val COMMUNITY_PRODUCT_CLICK = 6
        const val COMMUNITY_IMAGE_CLICK = 7
        const val COMMUNITY_PROFILE_CLICK = 8

        const val COMMUNITY_REPLY = 0
        const val COMMUNITY_REPLY_CHILD = 1
        const val COMMUNITY_SHOW_CHILD = 2

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, Community::class.java)
        post_id = data.id
        val parent = mutableListOf<Community>()
        parent.add(data)
        adapterParent.replaceAll(parent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        img_comment_gallery.setOnClickListener { launchPickPhotoIntent() }

        img_comment_sent.setOnClickListener {
            if (checkRequireFields(edt_comment.text.toString())) {
                showProgressDialog()
                viewModel.postCommentCommunity(post_id, edt_comment.text.toString(), postMedias, parentId)
            }
        }

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<CommunityComment> {
            @SuppressLint("SetTextI18n")
            override fun click(position: Int, data: CommunityComment, code: Int) {
                when (code) {
                    COMMUNITY_REPLY -> {
                        tv_reply.visibility = View.VISIBLE
                        tv_reply.text = "Trả lời bình luận của ${data.accountName}"
                        parentId = data.id
                    }

                    COMMUNITY_REPLY_CHILD -> {
                        tv_reply.visibility = View.VISIBLE
                        tv_reply.text = "Trả lời bình luận của ${data.lastComment?.accountName
                                ?: ""}"
                        parentId = data.id
                    }

                    COMMUNITY_SHOW_CHILD -> {
                        childViewModel.showCommentChild(data)
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
        setupParentRecycleview()
        swipe.setOnRefreshListener(this)
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

    private fun setupParentRecycleview() {
        rv_community_parent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_community_parent.adapter = adapterParent
        adapterParent.listener = object : ClickableAdapter.BaseAdapterAction<Community> {
            override fun click(position: Int, data: Community, code: Int) {
                when (code) {
                    COMMUNITY_LIKE_CLICK -> {
                        viewModel.postCommunityLike(data.id)
                    }

                    COMMUNITY_SHARE_NUMBER_CLICK -> openDialogShare(data)

                    COMMUNITY_SHARE_PRODUCT_CLICK -> openDialogShare(data)


                    COMMUNITY_PRODUCT_CLICK -> {
                        val productId = data.product?.id ?: -1L
                        if (productId != -1L) {
                            context?.let {
                                val intent = Intent(it, ProductDetailActivity::class.java)
                                intent.putExtra(Const.TransferKey.EXTRA_ID, productId)
                                startActivity(intent)
                            }
                        }
                    }

                    COMMUNITY_IMAGE_CLICK -> {
                        val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, data.images!!.toTypedArray())
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun openDialogShare(data: Community) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_community_share, false)
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()
            val tv_share_facebook = dialog.findViewById(R.id.tv_share_facebook) as VectorSupportTextView
            tv_share_facebook.setOnClickListener {
                shareFacebook(data, dialog)
            }
            val tv_share_zalo = dialog.findViewById(R.id.tv_share_zalo) as VectorSupportTextView
            tv_share_zalo.setOnClickListener {
                shareApp(data)
            }
            dialog.show()
        }
    }

    private var callbackManager: CallbackManager? = null

    private fun shareFacebook(data: Community, dialog: Dialog) {
        callbackManager = CallbackManager.Factory.create()
        val shareDialog = ShareDialog(this)

        shareDialog.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
            override fun onSuccess(result: Sharer.Result?) {
                dialog.dismiss()
            }

            override fun onCancel() {
                dialog.dismiss()
                toast("Chia sẻ bị huỷ bỏ")
            }

            override fun onError(error: FacebookException?) {
                dialog.dismiss()
                toast(error.toString())
            }
        })

        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            if (data.product != null) {
                val urlToShare = data.product?.link ?: ""
                val shareContent = ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(urlToShare))
                        .setQuote(data.content)
                        .build()

                shareDialog.show(shareContent)
            }

            if (data.images != null && data.images!!.isNotEmpty() && data.product == null) {
                if (data.images!!.size > 1) {
                    val thread = Thread(Runnable {
                        try {
                            val listSharePhoto = mutableListOf<SharePhoto>()
                            if (data.images!!.size > 5)
                                for (i in 0..5)
                                    try {
                                        val url = URL(data.images!![i])
                                        val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                                        val sharePhoto = SharePhoto.Builder().setBitmap(image).build()
                                        listSharePhoto.add(sharePhoto)
                                    } catch (e: IOException) {
                                        Log.d("IOException", e.toString())
                                    }
                            else {
                                for (i in data.images!!.indices)
                                    try {
                                        val url = URL(data.images!![i])
                                        val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                                        val sharePhoto = SharePhoto.Builder().setBitmap(image).build()
                                        listSharePhoto.add(sharePhoto)
                                    } catch (e: IOException) {
                                        Log.d("IOException", e.toString())
                                    }
                            }
                            val shareContent = SharePhotoContent.Builder()
                                    .addPhotos(listSharePhoto)
                                    .build()
                            shareDialog.show(shareContent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    })

                    thread.start()

                } else Glide.with(context)
                        .asBitmap()
                        .load(data.images!![0])
                        .into(object : SimpleTarget<Bitmap>(300, 300) {
                            override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                                val sharePhoto = SharePhoto.Builder().setBitmap(resource).build()
                                val shareContent = SharePhotoContent.Builder()
                                        .addPhoto(sharePhoto)
                                        .build()
                                shareDialog.show(shareContent)
                            }
                        })
            }

            if (data.product == null && data.images == null) {
                val shareContent = ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("http://hangviet360.com/cong-dong"))
                        .setQuote(data.content)
                        .build()

                shareDialog.show(shareContent)
            }
        }
    }

    private fun shareApp(data: Community) {
        val urlToShare = if (data.images != null && data.images!!.isNotEmpty()) {
            if (data.images!!.size > 1) {
                var linkImage = ""
                for (i in data.images!!.indices) {
                    linkImage += "${data.images!![i]}\n\n"
                }

                "${data.content}\n $linkImage\n ${data.product?.link
                        ?: ""}"

            } else {
                "${data.content}\n ${data.images!![0]}\n ${data.product?.link
                        ?: ""}"
            }
        } else
            "${data.content}\n ${data.product?.link ?: ""}"

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, urlToShare)
        startActivity(shareIntent)
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
        childViewModel = obtainViewModel(CommentViewModel::class.java, true)

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
                parentId = -1L
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
        firstLoad.post_id = post_id
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.last_id = 0
        viewModel.loadCommentCommunity(firstLoad)
    }

    fun loadMore() {
        reloadData = false
        val loadMore = LoadMoreCommunityRequest()
        loadMore.post_id = post_id
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.last_id = last_id
        viewModel.loadCommentCommunity(loadMore)
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