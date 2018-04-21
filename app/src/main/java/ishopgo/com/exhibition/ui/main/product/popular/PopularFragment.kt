package ishopgo.com.exhibition.ui.main.product.popular

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
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class PopularFragment : BaseListFragment<List<ProductProvider>, ProductProvider>() {

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    override fun populateData(data: List<ProductProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
        } else {
            adapter.addAll(data)
        }
    }

    override fun itemAdapter(): ClickableAdapter<ProductProvider> {
        return ProductAdapter()
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
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProvider> {
            override fun click(position: Int, data: ProductProvider, code: Int) {
                context?.let {
                    if (data is IdentityData) {
                        val intent = Intent(it, ProductDetailActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                        startActivity(intent)
                    }
                }
            }
        }

    }

    override fun obtainViewModel(): BaseListViewModel<List<ProductProvider>> {
        return obtainViewModel(PopularProductsViewModel::class.java, false)
    }
}
