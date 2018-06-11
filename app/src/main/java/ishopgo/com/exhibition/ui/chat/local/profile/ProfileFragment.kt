package ishopgo.com.exhibition.ui.chat.local.profile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchCommunityRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Const.TransferKey.EXTRA_ID
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.community.Community
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.community.CommunityProvider
import ishopgo.com.exhibition.ui.community.comment.CommunityCommentActivity
import ishopgo.com.exhibition.ui.login.LoginSelectOptionActivity
import ishopgo.com.exhibition.ui.main.home.search.community.detail.CommunityParentAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.content_local_chat_profile.*
import kotlinx.android.synthetic.main.empty_list_result.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

/**
 * Created by xuanhong on 4/6/18. HappyCoding!
 */
class ProfileFragment : BaseActionBarFragment() {

    private lateinit var viewModel: ProfileViewModel
    private var memberId = 0L
    private var last_id: Long = 0
    private var adapter = CommunityParentAdapter()
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
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
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

        viewModel = obtainViewModel(ProfileViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.userData.observe(this, Observer { info ->
            info?.let {
                showDetail(it)
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
                        if (community is Community)
                            last_id = community.id
                    }

                if (reloadData) {
                    if (it != null)
                        if (it.isEmpty()) {
                            view_empty_result_notice.visibility = View.VISIBLE
                            view_empty_result_notice.text = "Nội dung trống"
                        } else view_empty_result_notice.visibility = View.GONE
                    it?.let { it1 -> adapter.replaceAll(it1) }
                    view_recyclerview.scheduleLayoutAnimation()
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
                        view_avatar.setBackgroundResource(R.color.md_grey_200)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        view_avatar.setBackgroundResource(0)
                        return false
                    }

                })
                .into(view_avatar)

        view_name.text = info.provideName()
        view_phone.text = info.providePhone()
        view_dob.text = info.provideDob()
        view_email.text = info.provideEmail()
        view_company.text = info.provideCompany()
        view_region.text = info.provideRegion()
        view_address.text = info.provideAddress()
        view_type.text = info.provideType()
        view_joined_date.text = info.provideJoinedDate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setCustomTitle("Thông tin thành viên")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener { activity?.finish() }

        if (UserDataManager.currentType == "Chủ hội chợ") {
            toolbar.rightButton(R.drawable.ic_delete_green_24dp)
            toolbar.setRightButtonClickListener {
                context?.let {
                    MaterialDialog.Builder(it)
                            .content("Bạn có muốn xoá thành viên này không?")
                            .positiveText("Có")
                            .onPositive { _, _ ->
                                activity?.let {
                                    val memberId = it.intent.getLongExtra(Const.TransferKey.EXTRA_ID, -1L)
                                    viewModel.deleteMember(memberId)
                                }
                                showProgressDialog()
                            }
                            .negativeText("Không")
                            .onNegative { dialog, _ -> dialog.dismiss() }
                            .show()
                }
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

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<CommunityProvider> {
            override fun click(position: Int, data: CommunityProvider, code: Int) {
                when (code) {
                    COMMUNITY_LIKE_CLICK -> {
                        if (data is Community) {
                            viewModel.postCommunityLike(data.id)
                        }
                    }

                    COMMUNITY_COMMENT_CLICK -> {
                        if (UserDataManager.currentUserId > 0) {
                            if (data is Community) {
                                val intent = Intent(context, CommunityCommentActivity::class.java)
                                intent.putExtra(EXTRA_ID, data.id)
                                startActivity(intent)
                            }
                        } else
                            openLoginActivity()
                    }

                    COMMUNITY_SHARE_NUMBER_CLICK -> openDialogShare(data)


                    COMMUNITY_SHARE_PRODUCT_CLICK -> openDialogShare(data)


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

    private fun openLoginActivity() {
        val intent = Intent(context, LoginSelectOptionActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
        startActivity(intent)
    }

    private fun openDialogShare(data: CommunityProvider) {
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
}