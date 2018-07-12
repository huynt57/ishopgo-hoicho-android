package ishopgo.com.exhibition.ui.main.home.category.product.search

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchProductRequest
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.home.search.product.SearchProductViewModel
import ishopgo.com.exhibition.ui.main.product.SearchProductNavAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 6/19/18. HappyCoding!
 */
class SearchProductsOfCategoryFragment : BaseSearchActionBarFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val TAG = "SearchProducts"

        fun newInstance(arg: Bundle): SearchProductsOfCategoryFragment {
            val f = SearchProductsOfCategoryFragment()
            f.arguments = arg
            return f
        }
    }

    private var searchKey = ""
    private lateinit var adapter: SearchProductNavAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var viewModel: SearchProductViewModel
    private lateinit var currentCategory: Category

    override fun onRefresh() {
        swipe.isRefreshing = false
        firstLoad()
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_search_swipable_recyclerview
    }

    override fun triggerSearch(key: String) {
        searchKey = key
        firstLoad()
    }

    override fun dismissSearch() {
        activity?.onBackPressed()
    }

    override fun cancelSearch() {
        searchKey = ""
        firstLoad()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchField().hint = "Tìm kiếm trong ${currentCategory.name}"
        search_total.visibility = if (searchKey.isEmpty()) View.GONE else View.VISIBLE

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.dataReturned.observe(this, Observer { data ->
            data?.let {
                populateData(it)

                finishLoading()
            }
        })
        viewModel.total.observe(this, Observer { p ->
            p.let {
                if (it != null) {
                    search_total.visibility = if (searchKey.isEmpty()) View.GONE else View.VISIBLE
                    search_total.text = "${it} kết quả"
                }
            }
        })

        firstLoad()
    }

    private fun firstLoad() {
        reloadData = true
        adapter.clear()
        scrollListener.resetState()

        val firstLoad = SearchProductRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.keyword = searchKey
        firstLoad.categoryId = currentCategory.id
        viewModel.loadData(firstLoad)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false

        val loadMore = SearchProductRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.keyword = searchKey
        loadMore.categoryId = currentCategory.id
        viewModel.loadData(loadMore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(SearchProductViewModel::class.java, false)

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        currentCategory = Toolbox.gson.fromJson(json, Category::class.java)
    }

    private fun populateData(data: List<Product>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    private fun finishLoading() {
        swipe.isRefreshing = false
    }

}