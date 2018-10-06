package ishopgo.com.exhibition.ui.main.brand.popular

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchBrandsRequest
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.home.search.brands.SearchBrandsAdapter
import ishopgo.com.exhibition.ui.main.home.search.brands.SearchBrandsViewModel
import ishopgo.com.exhibition.ui.main.product.branded.ProductsOfBrandActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class PopularBrandsFragmentActionBar : BaseSearchActionBarFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun newInstance(params: Bundle): PopularBrandsFragmentActionBar {
            val fragment = PopularBrandsFragmentActionBar()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onRefresh() {
        swipe.isRefreshing = false
        firstLoad()
    }

    private var total: Int = 0
    private var keyword = ""
    private var adapter = SearchBrandsAdapter()
    private lateinit var viewModel: SearchBrandsViewModel
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(SearchBrandsViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.dataReturned.observe(this, Observer { d ->
            d?.let {
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

        viewModel.total.observe(this, Observer { p ->
            p.let {
                total = it ?: 0
                search_total.visibility = View.VISIBLE
                search_total.text = "$total kết quả được tìm thấy"
            }
        })
        firstLoad()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchField().hint = "Tìm kiếm thương hiệu"

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Brand> {
            override fun click(position: Int, data: Brand, code: Int) {
                context?.let {
                    showProductsOfBrand(data)
                }
            }
        }

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.adapter = adapter
        view_recyclerview.setHasFixedSize(true)
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

    private fun showProductsOfBrand(brand: Brand) {
        val intent = Intent(context, ProductsOfBrandActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(brand))
        startActivity(intent)
    }

    private fun firstLoad() {
        reloadData = true
        adapter.clear()
        scrollListener.resetState()

        val request = SearchBrandsRequest()
        request.name = keyword
        request.offset = 0
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false

        val request = SearchBrandsRequest()
        request.name = keyword
        request.offset = currentCount
        request.limit = Const.PAGE_LIMIT
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