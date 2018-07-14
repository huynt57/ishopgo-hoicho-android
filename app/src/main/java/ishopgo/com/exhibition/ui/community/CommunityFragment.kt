package ishopgo.com.exhibition.ui.community

import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.LinearLayoutManager
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
import ishopgo.com.exhibition.domain.request.SearchCommunityRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Const.TransferKey.EXTRA_JSON
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.community.Community
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.community.comment.CommunityCommentActivity
import ishopgo.com.exhibition.ui.community.notification.CommunityNotificationActivity
import ishopgo.com.exhibition.ui.community.share.CommunityShareActivity
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.login.LoginActivity
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_search_community.*
import java.io.IOException
import java.net.URL


/**
 * Created by hoangnh on 4/23/2018.
 */
class CommunityFragment : BaseListFragment<List<Community>, Community>() {
    override fun initLoading() {
        firstLoad()
    }

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_community, container, false)
    }

    private var last_id: Long = 0
    private var keyword = ""

    override fun populateData(data: List<Community>) {
        hideProgressDialog()

        if (data.isNotEmpty()) {
            val community = data[data.size - 1]
            last_id = community.id
        }

        if (keyword.isNotEmpty()) cardView_total.visibility = View.VISIBLE

        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
            adapter.addData(0, Community())
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data)
        }
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<Community> {
        val adapter = CommunityAdapter()
        adapter.addData(Community())
        return adapter
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = SearchCommunityRequest()
        firstLoad.content = keyword
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.last_id = 0
        viewModel.loadData(firstLoad)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = SearchCommunityRequest()
        loadMore.content = keyword
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.last_id = last_id
        viewModel.loadData(loadMore)
    }

    override fun obtainViewModel(): BaseListViewModel<List<Community>> {
        return obtainViewModel(CommunityViewModel::class.java, false)
    }

    companion object {
        const val TAG = "CommunityFragment"

        const val COMMUNITY_SHARE_CLICK = 1
        const val COMMUNITY_LIKE_CLICK = 2
        const val COMMUNITY_COMMENT_CLICK = 3
        const val COMMUNITY_SHARE_NUMBER_CLICK = 4
        const val COMMUNITY_SHARE_PRODUCT_CLICK = 5
        const val COMMUNITY_PRODUCT_CLICK = 6
        const val COMMUNITY_IMAGE_CLICK = 7
        const val COMMUNITY_PROFILE_CLICK = 8
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<Community>) {
            (adapter as ClickableAdapter<Community>).listener = object : ClickableAdapter.BaseAdapterAction<Community> {
                override fun click(position: Int, data: Community, code: Int) {
                    when (code) {
                        COMMUNITY_SHARE_CLICK -> {
                            if (UserDataManager.currentUserId > 0) {
                                val intent = Intent(context, CommunityShareActivity::class.java)
                                startActivityForResult(intent, Const.RequestCode.SHARE_POST_COMMUNITY)
                            } else {
                                val intent = Intent(context, LoginActivity::class.java)
                                intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
                                startActivity(intent)
                            }
                        }

                        COMMUNITY_LIKE_CLICK -> {
                            if (viewModel is CommunityViewModel) (viewModel as CommunityViewModel).postCommunityLike(data.id)
                        }

                        COMMUNITY_COMMENT_CLICK -> {
                            if (UserDataManager.currentUserId > 0) {

                                val intent = Intent(context, CommunityCommentActivity::class.java)
                                intent.putExtra(EXTRA_JSON, Toolbox.gson.toJson(data))
                                startActivity(intent)
                            } else openLoginActivity()
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

                        COMMUNITY_PROFILE_CLICK -> {
                            val intent = Intent(view.context, MemberProfileActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        img_cancel_search.setOnClickListener {
            keyword = ""
            cardView_total.visibility = View.GONE
            firstLoad()
        }
    }

    private fun openLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
        startActivity(intent)
    }

    fun openDialogSearch() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Tìm kiếm")
                    .customView(R.layout.dialog_search_community, false)
                    .positiveText("Lọc")
                    .onPositive { dialog, _ ->
                        val edit_keyword = dialog.findViewById(R.id.edit_keyword) as TextInputEditText

                        keyword = edit_keyword.text.toString().trim { it <= ' ' }

                        dialog.dismiss()

                        showProgressDialog()
                        firstLoad()
                    }
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val edit_keyword = dialog.findViewById(R.id.edit_keyword) as TextInputEditText
            edit_keyword.setText(keyword)


            dialog.show()
        }
    }

    fun openNotificationActivity() {
        if (UserDataManager.currentUserId > 0) {
            val intent = Intent(context, CommunityNotificationActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
            startActivity(intent)
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
                        .setContentUrl(Uri.parse(String.format("%s://%s/cong-dong", getString(R.string.app_protocol), getString(R.string.app_host))))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.SHARE_POST_COMMUNITY && resultCode == Activity.RESULT_OK) {
            firstLoad()
        }
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (viewModel as CommunityViewModel).total.observe(this, Observer { p ->
            p.let {
                if (it != null)
                    tv_total.text = "$it kết quả được tìm thấy"
            }
        })
    }
}