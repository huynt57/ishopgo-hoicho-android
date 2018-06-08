package ishopgo.com.exhibition.ui.community

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreCommunityRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Const.TransferKey.EXTRA_ID
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.community.Community
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.ProfileActivity
import ishopgo.com.exhibition.ui.community.comment.CommunityCommentActivity
import ishopgo.com.exhibition.ui.community.share.CommunityShareActivity
import ishopgo.com.exhibition.ui.login.LoginSelectOptionActivity
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by hoangnh on 4/23/2018.
 */
class CommunityFragment : BaseListFragment<List<CommunityProvider>, CommunityProvider>() {

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private var last_id: Long = 0

    override fun populateData(data: List<CommunityProvider>) {
        if (data.isNotEmpty()) {
            val community = data[data.size - 1]
            if (community is Community)
                last_id = community.id
        }

        if (reloadData) {
            adapter.replaceAll(data)
            adapter.addData(0, Community())
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data)
        }
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<CommunityProvider> {
        val adapter = CommunityAdapter()
        adapter.addData(Community())
        return adapter
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = LoadMoreCommunityRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.last_id = 0
        viewModel.loadData(firstLoad)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = LoadMoreCommunityRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.last_id = last_id
        viewModel.loadData(loadMore)
    }

    override fun obtainViewModel(): BaseListViewModel<List<CommunityProvider>> {
        return obtainViewModel(CommunityViewModel::class.java, false)
    }

    companion object {
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
        if (adapter is ClickableAdapter<CommunityProvider>) {
            (adapter as ClickableAdapter<CommunityProvider>).listener = object : ClickableAdapter.BaseAdapterAction<CommunityProvider> {
                override fun click(position: Int, data: CommunityProvider, code: Int) {
                    when (code) {
                        COMMUNITY_SHARE_CLICK -> {
                            if (UserDataManager.currentUserId > 0) {
                                val intent = Intent(context, CommunityShareActivity::class.java)
                                startActivityForResult(intent, Const.RequestCode.SHARE_POST_COMMUNITY)
                            } else {
                                showDiglogLogin()
                            }
                        }

                        COMMUNITY_LIKE_CLICK -> {
                            if (data is Community) {
                                if (viewModel is CommunityViewModel) (viewModel as CommunityViewModel).postCommunityLike(data.id)
                            }
                        }

                        COMMUNITY_COMMENT_CLICK -> {
                            if (data is Community) {
                                val intent = Intent(context, CommunityCommentActivity::class.java)
                                intent.putExtra(EXTRA_ID, data.id)
                                startActivity(intent)
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

                        COMMUNITY_PROFILE_CLICK -> {
                            if (data is Community) {
                                val intent = Intent(view.context, ProfileActivity::class.java)
                                intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                                startActivity(intent)
                            }
                        }
                    }
                }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.SHARE_POST_COMMUNITY && resultCode == Activity.RESULT_OK) {
            firstLoad()
        }
    }
}