package ishopgo.com.exhibition.ui.main.brandmanager.search

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.BrandsRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.brandmanager.BrandManagerViewModel
import ishopgo.com.exhibition.ui.main.home.search.brands.SearchBrandsAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

class BrandSearchFragment : BaseSearchActionBarFragment(), SwipeRefreshLayout.OnRefreshListener, BackpressConsumable {
    private var keyword = ""
    private val adapter = SearchBrandsAdapter()
    private lateinit var viewModel: BrandManagerViewModel
    private lateinit var viewModelSearch: BrandSearchViewModel
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    companion object {
        const val TAG = "BrandSearchFragment"

        fun newInstance(params: Bundle): BrandSearchFragment {
            val fragment = BrandSearchFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private fun firstLoad() {
        reloadData = true
        adapter.clear()
        scrollListener.resetState()
        val firstLoad = BrandsRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.name = keyword
        viewModel.loadData(firstLoad)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false
        val loadMore = BrandsRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.name = keyword
        viewModel.loadData(loadMore)
    }

    override fun onRefresh() {
        swipe.isRefreshing = false
        firstLoad()
    }

    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
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

    override fun openFilter() {
    }

    override fun showFilter(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Brand> {
            override fun click(position: Int, data: Brand, code: Int) {
                viewModelSearch.getDataSearchBrands(data)
                activity?.onBackPressed()
            }

        }
        view_recyclerview.adapter = adapter
        view_recyclerview.setHasFixedSize(true)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
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
        viewModel = obtainViewModel(BrandManagerViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.total.observe(this, Observer { p ->
            p.let {
                search_total.visibility = View.VISIBLE
                search_total.text = "${it ?: 0} kết quả được tìm thấy"
            }
        })

        viewModel.dataReturned.observe(this, Observer { data ->
            data?.let {
                populateData(it)

                finishLoading()
            }
        })

        viewModelSearch = obtainViewModel(BrandSearchViewModel::class.java, true)
        viewModelSearch.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        firstLoad()
    }

    private fun populateData(data: List<Brand>) {
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