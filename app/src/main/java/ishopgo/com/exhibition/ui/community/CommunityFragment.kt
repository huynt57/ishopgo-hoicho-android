package ishopgo.com.exhibition.ui.community

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreLastIdRequestParams
import ishopgo.com.exhibition.model.Community.Community
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.login.LoginSelectOptionActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
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
            last_id = data[data.size - 1].providerId()
        }

        if (reloadData) {
            adapter.replaceAll(data)
            adapter.addData(0, Community())
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
        val firstLoad = LoadMoreLastIdRequestParams()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.last_id = 0
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = LoadMoreLastIdRequestParams()
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
                                val builder = context?.let { AlertDialog.Builder(it) }
                                builder?.setTitle("Thông báo")
                                builder?.setMessage("Bạn cần đăng nhập để sử dụng tính năng này!")
                                builder?.setPositiveButton("Đăng nhập") { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(context, LoginSelectOptionActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish()
                                }

                                builder?.setNegativeButton("Bỏ qua") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                val dialog = builder?.create()
                                dialog?.show()

                                val positiveButton = dialog?.getButton(AlertDialog.BUTTON_POSITIVE)
                                positiveButton?.setTextColor(Color.parseColor("#00c853"))

                                val negativeButton = dialog?.getButton(AlertDialog.BUTTON_NEGATIVE)
                                negativeButton?.setTextColor(Color.parseColor("#00c853"))

                            }
                        }

                        COMMUNITY_LIKE_CLICK -> {
                            toast("Vào xem chi tiết sản phẩm")
                        }

                        COMMUNITY_COMMENT_CLICK -> {
                            toast("Vào xem chi tiết sản phẩm")
                        }

                        COMMUNITY_SHARE_NUMBER_CLICK -> {
                            toast("Vào xem chi tiết sản phẩm")
                        }

                        COMMUNITY_SHARE_PRODUCT_CLICK -> {
                            context?.let {
                                val dialog = MaterialDialog.Builder(it)
                                        .customView(R.layout.dialog_community_share, false)
                                        .autoDismiss(false)
                                        .canceledOnTouchOutside(true)
                                        .build()
                                dialog.show()
                            }
                        }

                        COMMUNITY_PRODUCT_CLICK -> {
                            toast("Vào xem chi tiết sản phẩm")
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.SHARE_POST_COMMUNITY && resultCode == Activity.RESULT_OK) {
            firstLoad()
        }
    }
}