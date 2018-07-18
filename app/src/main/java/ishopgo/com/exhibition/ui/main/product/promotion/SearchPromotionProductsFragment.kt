package ishopgo.com.exhibition.ui.main.product.promotion

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchByNameRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.home.search.product.SearchProductAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

class SearchPromotionProductsFragment: BaseSearchActionBarFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        swipe.isRefreshing = false
        firstLoad()
    }

    private var keyword = ""
    private lateinit var adapter: SearchProductAdapter
    private lateinit var viewModel: PromotionProductsViewModel
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(PromotionProductsViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.dataReturned.observe(this, Observer { d ->
            d?.let {
                val products = it
                if (reloadData) {
                    adapter.replaceAll(products)
                }
                else {
                    adapter.addAll(products)
                }
            }
        })
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchField().hint = "Tên sản phẩm"

        adapter = SearchProductAdapter()
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                context?.let {
                    val intent = Intent(it, ProductDetailActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                    startActivity(intent)
                }
            }

        }
        view_recyclerview.adapter = adapter
        view_recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore(totalItemsCount)
            }
        }
        view_recyclerview.addOnScrollListener(scrollListener)

        swipe.setOnRefreshListener(this)
    }

    private fun firstLoad() {
        reloadData = true
        adapter.clear()
        scrollListener.resetState()

        val firstLoad = SearchByNameRequest ()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.name = keyword
        viewModel.loadData(firstLoad)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false

        val request = SearchByNameRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = currentCount
        request.name = keyword
        viewModel.loadData(request)
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_search_swipable_recyclerview
    }

    override fun triggerSearch(key: String) {
        keyword = key
        firstLoad()
    }

    override fun searchReset() {
        keyword = ""
        firstLoad()
    }

    override fun dismissSearch() {
        activity?.onBackPressed()
    }


}