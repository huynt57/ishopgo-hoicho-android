package ishopgo.com.exhibition.ui.community

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequestParams
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
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

    override fun populateData(data: List<CommunityProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
        } else {
            adapter.addAll(data)
        }
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<CommunityProvider> {
        return CommunityAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = LoadMoreRequestParams()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = LoadMoreRequestParams()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
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
                            val intent = Intent(context, CommunityShareActivity::class.java)
                            startActivity(intent)
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
}