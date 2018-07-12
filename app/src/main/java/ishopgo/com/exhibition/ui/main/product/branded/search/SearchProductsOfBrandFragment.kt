package ishopgo.com.exhibition.ui.main.product.branded.search

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.BrandProductsRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.SearchProductNavAdapter
import ishopgo.com.exhibition.ui.main.product.branded.ProductsOfBrandViewModel
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 6/25/18. HappyCoding!
 */
class SearchProductsOfBrandFragment : BaseSearchActionBarFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        swipe.isRefreshing = false
        firstLoad()
    }

    companion object {

        const val TAG = "SearchProductsOfBrandFragment"

        fun newInstance(args: Bundle): SearchProductsOfBrandFragment {
            val f = SearchProductsOfBrandFragment()
            f.arguments = args

            return f
        }
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_search_swipable_recyclerview
    }

    override fun triggerSearch(key: String) {
        keyword = key
        firstLoad()
    }

    override fun cancelSearch() {
        keyword = ""
        firstLoad()
    }

    override fun dismissSearch() {
        activity?.onBackPressed()
    }


    private lateinit var viewModel: ProductsOfBrandViewModel
    private lateinit var brand: Brand
    private var keyword = ""
    private lateinit var adapter: SearchProductNavAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(ProductsOfBrandViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.dataReturned.observe(this, Observer { d ->
            d?.let {
                val products = it.data ?: listOf()
                if (reloadData) {
                    adapter.replaceAll(products)
                }
                else {
                    adapter.addAll(products)
                }

                search_total.visibility = View.VISIBLE
                search_total.text = "${it.total ?: 0} kết quả"
            }
        })

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        brand = Toolbox.gson.fromJson(json, Brand::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchField().hint = "Tìm sản phẩm của  ${brand.name}"
        search_total.visibility = if (keyword.isEmpty()) View.GONE else View.VISIBLE

        adapter = SearchProductNavAdapter()
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

        val firstLoad = BrandProductsRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.name = keyword
        firstLoad.brandId = brand.id
        viewModel.loadData(firstLoad)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false

        val request = BrandProductsRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = currentCount
        request.name = keyword
        request.brandId = brand.id
        viewModel.loadData(request)
    }

}