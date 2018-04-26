package ishopgo.com.exhibition.ui.main.brand.popular

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequestParams
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandAdapter
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider
import ishopgo.com.exhibition.ui.main.product.shop.ProductsOfShopActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class PopularFragment : BaseListFragment<List<HighlightBrandProvider>, HighlightBrandProvider>() {

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    override fun populateData(data: List<HighlightBrandProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
        } else {
            adapter.addAll(data)
        }
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<HighlightBrandProvider> {
        return HighlightBrandAdapter()
    }

    override fun firstLoad() {
        val loadMore = LoadMoreRequestParams()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = 0
        viewModel.loadData(loadMore)
    }

    override fun loadMore(currentCount: Int) {
        val loadMore = LoadMoreRequestParams()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.loadData(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<HighlightBrandProvider>) {
            (adapter as ClickableAdapter<HighlightBrandProvider>).listener = object : ClickableAdapter.BaseAdapterAction<HighlightBrandProvider> {
                override fun click(position: Int, data: HighlightBrandProvider, code: Int) {
                    context?.let {
                        if (data is IdentityData) {
                            val intent = Intent(it, ProductsOfShopActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                            startActivity(intent)
                        }
                    }
                }
            }
        }

    }

    override fun obtainViewModel(): BaseListViewModel<List<HighlightBrandProvider>> {
        return obtainViewModel(PopularBrandsViewModel::class.java, false)
    }

}