package ishopgo.com.exhibition.ui.community

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchCommunityRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

/**
 * Created by xuanhong on 7/19/18. HappyCoding!
 */
class SearchCommunityFragmentActionBar : BaseSearchActionBarFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun openFilter() {
    }

    override fun showFilter(): Boolean {
        return false
    }

    override fun onRefresh() {
        swipe.isRefreshing = false
        firstLoad()
    }

    companion object {
        const val TAG = "SearchCommunity"
    }

    private var keyword = ""
    private var last_id: Long = -1L
    private lateinit var adapter: SearchCommunityAdapter
    private lateinit var viewModel: CommunityViewModel
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(CommunityViewModel::class.java, false)
        viewModel.errorSignal.observe(viewLifeCycleOwner!!, Observer { error -> error?.let { resolveError(it) } })
        viewModel.dataReturned.observe(viewLifeCycleOwner!!, Observer { d ->
            d?.let {
                if (it.isNotEmpty()) {
                    val community = it[it.size - 1]
                    last_id = community.id
                }

                if (keyword.isNotEmpty()) search_total.visibility = View.VISIBLE

                if (reloadData) {
                    if (it.isEmpty()) {
                        view_empty_result_notice.visibility = View.VISIBLE
                        view_empty_result_notice.text = "Nội dung trống"
                    } else view_empty_result_notice.visibility = View.GONE

                    adapter.replaceAll(it)
                    view_recyclerview.scheduleLayoutAnimation()
                } else {
                    adapter.addAll(it)
                }

            }
        })
        viewModel.total.observe(viewLifeCycleOwner!!, Observer { p ->
            p.let {
                if (it != null)
                    search_total.text = "$it kết quả được tìm thấy"
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchField().hint = "Tìm trong cộng đồng"

        adapter = SearchCommunityAdapter()
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

        val firstLoad = SearchCommunityRequest()
        firstLoad.content = keyword
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.last_id = 0
        viewModel.loadData(firstLoad)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false

        if (last_id != -1L) {
            val loadMore = SearchCommunityRequest()
            loadMore.content = keyword
            loadMore.limit = Const.PAGE_LIMIT
            loadMore.last_id = last_id
            viewModel.loadData(loadMore)
        }
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