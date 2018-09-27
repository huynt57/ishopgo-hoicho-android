package ishopgo.com.exhibition.ui.main.stamp.nostamp.assign

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchProductRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.home.search.product.SearchProductAdapter
import ishopgo.com.exhibition.ui.main.stamp.nostamp.NoStampViewModel
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

class NoStampSearchProductFragment : BaseSearchActionBarFragment(), SwipeRefreshLayout.OnRefreshListener {
    private var searchKey = ""
    private var stampId = 0L
    private var adapter = SearchProductAdapter()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var viewModel: NoStampViewModel

    override fun contentLayoutRes(): Int {
        return R.layout.content_search_swipable_recyclerview
    }

    override fun triggerSearch(key: String) {
        searchKey = key
        firstLoad()
    }

    override fun searchReset() {
        searchKey = ""
        firstLoad()
    }

    override fun dismissSearch() {
        activity?.onBackPressed()
    }

    override fun onRefresh() {
        swipe.isRefreshing = false
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
        viewModel.searchProductAssign(firstLoad)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false

        val loadMore = SearchProductRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.keyword = searchKey
        viewModel.searchProductAssign(loadMore)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(NoStampViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.dataProductSearch.observe(this, Observer { d ->
            d?.let {
                if (reloadData) {
                    adapter.replaceAll(it)
                } else {
                    adapter.addAll(it)
                }

            }
        })

        viewModel.total.observe(this, Observer { p ->
            p?.let {
                search_total.visibility = View.VISIBLE
                search_total.text = "${it} sản phẩm"
            }
        })

        firstLoad()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchField().hint = "Tìm sản phẩm"
        search_total.visibility = if (searchKey.isEmpty()) View.GONE else View.VISIBLE

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                context?.let {
                    viewModel.resultProduct(data)
                    activity?.onBackPressed()
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

    companion object {
        const val TAG = "NoStampSearchProductFragment"
        fun newInstance(params: Bundle): NoStampSearchProductFragment {
            val fragment = NoStampSearchProductFragment()
            fragment.arguments = params

            return fragment
        }
    }
}