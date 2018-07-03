package ishopgo.com.exhibition.ui.chat.local.profile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
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
import ishopgo.com.exhibition.domain.request.CreateConversationRequest
import ishopgo.com.exhibition.domain.request.SearchCommunityRequest
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Const.TransferKey.EXTRA_ID
import ishopgo.com.exhibition.model.Profile
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.community.Community
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import ishopgo.com.exhibition.ui.community.comment.CommunityCommentActivity
import ishopgo.com.exhibition.ui.login.LoginActivity
import ishopgo.com.exhibition.ui.main.home.search.community.detail.CommunityParentAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.BottomSheetOptionListener
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.content_local_chat_profile.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import java.io.IOException
import java.net.URL

/**
 * Created by xuanhong on 4/6/18. HappyCoding!
 */
class MemberProfileFragment : BaseActionBarFragment() {

    private lateinit var viewModel: MemberProfileViewModel
    private var memberId = 0L
    private var last_id: Long = 0
    private var adapter = CommunityParentAdapter()
    private lateinit var currentProfile: Profile

    override fun contentLayoutRes(): Int {
        return R.layout.content_local_chat_profile
    }

    companion object {
        const val COMMUNITY_LIKE_CLICK = 2
        const val COMMUNITY_COMMENT_CLICK = 3
        const val COMMUNITY_SHARE_NUMBER_CLICK = 4
        const val COMMUNITY_SHARE_PRODUCT_CLICK = 5
        const val COMMUNITY_PRODUCT_CLICK = 6
        const val COMMUNITY_IMAGE_CLICK = 7
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            memberId = it.intent.getLongExtra(Const.TransferKey.EXTRA_ID, -1L)
        }
    }

    private fun firstLoad() {
        val firstLoad = SearchCommunityRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.last_id = 0
        firstLoad.account_id = memberId
        viewModel.loadProfileCommunity(firstLoad)
    }

    private fun loadMore() {
        val loadMore = SearchCommunityRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.last_id = last_id
        loadMore.account_id = memberId
        viewModel.loadProfileCommunity(loadMore)
    }


    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(MemberProfileViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.userData.observe(this, Observer { info ->
            info?.let {
                currentProfile = it
                showDetail(ConverterMemberProfile().convert(it))
            }
        })
        viewModel.upgradeSusscess.observe(this, Observer { ok ->
            ok?.let {
                toast("Nâng cấp thành công")
            }
        })
        viewModel.conversation.observe(this, Observer { c ->
            c?.let {
                val conv = LocalConversationItem()
                conv.idConversions = c.id ?: ""
                conv.name = c.name ?: ""
                context?.let {
                    val intent = Intent(it, ConversationActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_CONVERSATION_ID, conv.idConversions)
                    intent.putExtra(Const.TransferKey.EXTRA_TITLE, conv.name)
                    startActivity(intent)
                }
            }
        })

        viewModel.deleteSusscess.observe(this, Observer {
            toast("Xoá thành công")
            hideProgressDialog()
            activity?.setResult(RESULT_OK)
            activity?.finish()
        })

        viewModel.dataCommunity.observe(this, Observer { p ->
            p.let {
                if (it != null)
                    if (it.isNotEmpty()) {
                        val community = it[it.size - 1]
                        last_id = community.id
                    }

                if (reloadData) {
                    if (it != null)
                        if (it.isEmpty()) {
                            view_empty_result_notice.visibility = View.VISIBLE
                            view_empty_result_notice.text = "Nội dung trống"
                        } else view_empty_result_notice.visibility = View.GONE
                    it?.let { it1 -> adapter.replaceAll(it1) }
                } else {
                    it?.let { it1 -> adapter.addAll(it1) }
                }
            }
        })

        viewModel.loadUserDetail(memberId)
        firstLoad()
    }

    private fun showDetail(info: UserInfoProvider) {
        Glide.with(view_cover.context)
                .load(R.drawable.default_cover_background)
                .apply(RequestOptions().centerCrop()
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                )
                .into(view_cover)
        Glide.with(view_avatar.context)
                .load(info.provideAvatar())
                .apply(RequestOptions().centerCrop()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                )
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        if (isVisible)
                            view_avatar.setBackgroundResource(R.color.md_grey_200)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        if (isVisible)
                            view_avatar.setBackgroundResource(0)
                        return false
                    }

                })
                .into(view_avatar)

        view_name.text = info.provideName()
        view_info.text = info.info()
    }

    private fun callShop(phoneNumber: String) {
        val call = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_DIAL, call)
        if (intent.resolveActivity(requireContext().packageManager) != null)
            startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)

        toolbar.setCustomTitle("Thông tin thành viên")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        if (UserDataManager.currentType == "Chủ hội chợ") {
            view_setting.visibility = View.VISIBLE
            view_setting.setOnClickListener {
                val options = MemberProfileOptions()
                options.optionSelectedListener = object: BottomSheetOptionListener {
                    override fun click(code: Int) {
                        when (code) {
                            MemberProfileOptions.OPTION_UPGRADE -> {
                                val extra = Bundle()
                                extra.putLong(Const.TransferKey.EXTRA_ID, memberId)
                                Navigation.findNavController(view_setting).navigate(R.id.action_memberProfileFragment_to_upgradeToBoothFragment, extra)
//                                confirmUpgradeMember()
                            }
                            MemberProfileOptions.OPTION_DELETE -> {
                                confirmDeleteMember()
                            }
                            else -> {
                            }
                        }
                    }
                }
                options.show(childFragmentManager, "MemberProfileOptions")
            }
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.isNestedScrollingEnabled = false
        view_recyclerview.setHasFixedSize(false)
        view_recyclerview.adapter = adapter
        view_recyclerview.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore()
            }
        })

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Community> {
            override fun click(position: Int, data: Community, code: Int) {
                when (code) {
                    COMMUNITY_LIKE_CLICK -> {
                        viewModel.postCommunityLike(data.id)
                    }

                    COMMUNITY_COMMENT_CLICK -> {
                        if (UserDataManager.currentUserId > 0) {
                            val intent = Intent(context, CommunityCommentActivity::class.java)
                            intent.putExtra(EXTRA_ID, data.id)
                            startActivity(intent)
                        } else
                            openLoginActivity()
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

        view_call.setOnClickListener {
            if (!::currentProfile.isInitialized) return@setOnClickListener

            callShop(currentProfile.phone.toString())
        }
        view_message.setOnClickListener {
            if (!::currentProfile.isInitialized) return@setOnClickListener

            // start conversation
            val currentUserId = UserDataManager.currentUserId
            if (currentUserId != currentProfile.id) {
                val request = CreateConversationRequest()
                request.type = 1
                val members = mutableListOf<Long>()
                members.add(currentUserId)
                members.add(currentProfile.id)
                request.member = members
                viewModel.createConversation(request)
            }
        }
    }

    private fun confirmUpgradeMember() {
        MaterialDialog.Builder(requireContext())
                .content("Bạn có muốn nâng cấp thành viên này lên gian hàng không?")
                .positiveText("Có")
                .onPositive { _, _ ->
                    activity?.let {
                        val memberId = it.intent.getLongExtra(EXTRA_ID, -1L)
//                        viewModel.upgradeMemberToBooth(memberId)
                    }
                    showProgressDialog()
                }
                .negativeText("Không")
                .onNegative { dialog, _ -> dialog.dismiss() }
                .show()
    }

    private fun confirmDeleteMember() {
        MaterialDialog.Builder(requireContext())
                .content("Bạn có muốn xoá thành viên này không?")
                .positiveText("Có")
                .onPositive { _, _ ->
                    activity?.let {
                        val memberId = it.intent.getLongExtra(EXTRA_ID, -1L)
                        viewModel.deleteMember(memberId)
                    }
                    showProgressDialog()
                }
                .negativeText("Không")
                .onNegative { dialog, _ -> dialog.dismiss() }
                .show()
    }

    private fun openLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
        startActivity(intent)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
}