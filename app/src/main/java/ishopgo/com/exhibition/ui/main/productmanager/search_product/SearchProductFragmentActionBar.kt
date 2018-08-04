package ishopgo.com.exhibition.ui.main.productmanager.search_product

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchProductRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.FilterResult
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel
import ishopgo.com.exhibition.ui.main.productmanager.add.ProductManagerRelatedAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class SearchProductFragmentActionBar : BaseSearchActionBarFragment(), SwipeRefreshLayout.OnRefreshListener, BackpressConsumable {
    override fun showFilter(): Boolean {
        return true
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
        filterViewModel.openFilterSp(filterData)
    }


    override fun onBackPressConsumed(): Boolean {
        return hideKeyboard()
    }

    override fun onRefresh() {
        swipe.isRefreshing = false
        firstLoad()
    }

    private lateinit var filterViewModel: SearchProductManagerViewModel

    companion object {
        const val TAG = "SearchProductFragmentActionBar"

        fun newInstance(params: Bundle): SearchProductFragmentActionBar {
            val fragment = SearchProductFragmentActionBar()
            fragment.arguments = params

            return fragment
        }

        const val TYPE_SP_LIENQUAN: Int = 0
        const val TYPE_SP_VATTU: Int = 1
        const val TYPE_SP_GIAIPHAP: Int = 2

        const val TYPE_THUONGHIEU = 0
        const val TYPE_DANHMUC = 1
        const val TYPE_GIANHANG = 2
    }

    private var type = TYPE_SP_LIENQUAN
    private var filterData = mutableListOf<FilterResult>()
    private var keyword = ""
    private lateinit var viewModel: ProductManagerViewModel
    private var adapter = ProductManagerRelatedAdapter()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getInt(Const.TransferKey.EXTRA_REQUIRE, TYPE_SP_LIENQUAN) ?: TYPE_SP_LIENQUAN
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_search_swipable_recyclerview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                if (type == TYPE_SP_LIENQUAN)
                    filterViewModel.getDataSpLienQuan(data)
                if (type == TYPE_SP_VATTU)
                    filterViewModel.getDataSpVatTu(data)
                if (type == TYPE_SP_GIAIPHAP)
                    filterViewModel.getDataSpGiaiPhap(data)
                activity?.onBackPressed()
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

        container_receivers.setOnTagClickListener { view, position, parent ->
            run {
                if (position < filterData.size)
                    filterData.removeAt(position)
                else
                    filterData.removeAt(filterData.size - 1)

                parent.removeView(view)
                if (filterData.size == 0) {
                    container_receivers.visibility = View.GONE
                }
                firstLoad()
            }
            true
        }

        view_recyclerview.addOnScrollListener(scrollListener)

        swipe.setOnRefreshListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        filterViewModel = obtainViewModel(SearchProductManagerViewModel::class.java, true)

        filterViewModel.getFilterSp.observe(this, Observer { c ->
            c?.let {
                container_receivers.visibility = View.VISIBLE
                container_receivers.adapter = object : TagAdapter<FilterResult>(it) {
                    override fun getView(parent: FlowLayout?, position: Int, t: FilterResult?): View {
                        val view = layoutInflater.inflate(R.layout.add_tag_flow, container_receivers, false) as TextView
                        view.text = t?.name

                        val icCheckbox = AppCompatResources.getDrawable(view.context, R.drawable.ic_close_white_16dp)
                        view.setCompoundDrawablesWithIntrinsicBounds(null, null, icCheckbox, null)
                        return view
                    }

                }

                filterData = it
                firstLoad()
            }
        })

        viewModel = obtainViewModel(ProductManagerViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel.total.observe(this, Observer { p ->
            p.let {
                search_total.visibility = View.VISIBLE
                search_total.text = "${it ?: 0} kết quả được tìm thấy"
            }
        })

        viewModel.filterProduct.observe(this, Observer { p ->
            p?.let {
                if (it.isEmpty()) {
                    view_empty_result_notice.visibility = View.VISIBLE
                    view_empty_result_notice.text = "Nội dung trống"
                } else view_empty_result_notice.visibility = View.GONE

                if (reloadData) {
                    adapter.replaceAll(it)
                    view_recyclerview.scheduleLayoutAnimation()
                } else
                    adapter.addAll(it)
            }
        })
        firstLoad()
    }

    fun firstLoad() {
        reloadData = true
        adapter.clear()
        scrollListener.resetState()

        val firstLoad = SearchProductRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.keyword = keyword
        for (i in filterData.indices) {
            if (filterData[i].type == TYPE_GIANHANG)
                firstLoad.boothId = filterData[i].id
            if (filterData[i].type == TYPE_DANHMUC)
                firstLoad.categoryId = filterData[i].id
            if (filterData[i].type == TYPE_THUONGHIEU)
                firstLoad.brandId = filterData[i].id
        }
        viewModel.loadSearchProduct(firstLoad)
    }

    fun loadMore(currentCount: Int) {
        reloadData = false

        val loadMore = SearchProductRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.keyword = keyword
        for (i in filterData.indices) {
            if (filterData[i].type == TYPE_GIANHANG)
                loadMore.boothId = filterData[i].id
            if (filterData[i].type == TYPE_DANHMUC)
                loadMore.categoryId = filterData[i].id
            if (filterData[i].type == TYPE_THUONGHIEU)
                loadMore.brandId = filterData[i].id
        }
        viewModel.loadSearchProduct(loadMore)
    }
}