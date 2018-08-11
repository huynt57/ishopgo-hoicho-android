package ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.IcheckRequest
import ishopgo.com.exhibition.domain.response.IcheckSalePoint
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductViewModel
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckSalePointAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class IcheckSalePointFragment : BaseSearchActionBarFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun openFilter() {

    }

    override fun showFilter(): Boolean {
        return false
    }


    companion object {
        fun newInstance(params: Bundle): IcheckSalePointFragment {
            val f = IcheckSalePointFragment()
            f.arguments = params

            return f
        }
    }

    private var productCode = ""
    private var searchKey = ""
    private lateinit var adapter: IcheckSalePointAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var viewModel: IcheckProductViewModel

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

    override fun searchReset() {
        searchKey = ""
        firstLoad()
    }

    private fun openSalePointDetail() {
        toast("Đang phát triển")
//        val intent = Intent(context, ShopDetailActivity::class.java)
//        intent.putExtra(Const.TransferKey.EXTRA_ID, shopId)
//        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchField().hint = "Tìm kiếm điểm bán"
        search_total.visibility = View.GONE

        adapter = IcheckSalePointAdapter()
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<IcheckSalePoint> {
            override fun click(position: Int, data: IcheckSalePoint, code: Int) {
                openSalePointDetail()
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
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
        swipe.setOnRefreshListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(IcheckProductViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.dataSalePoint.observe(this, Observer { data ->
            data?.let {
                populateData(it.list ?: mutableListOf())

                search_total.visibility = View.VISIBLE
                search_total.text = "${it.meta?.total ?: 0} kết quả được tìm thấy"

                finishLoading()
            }
        })

        firstLoad()
    }

    fun firstLoad() {
        reloadData = true

        val page = 1
        val pageSize = Const.PAGE_LIMIT
        val requestSalePoint = String.format("https://gateway.icheck.com.vn/app/locations/%s?geo=%s&page=%s&page_size=%s&keyword=%s", productCode, "21.735235,121.850293", page, pageSize, searchKey)
        viewModel.loadSalePoint(requestSalePoint)
    }

    fun loadMore(currentCount: Int) {
        reloadData = false

        val pageSize = Const.PAGE_LIMIT
        val requestSalePoint = String.format("https://gateway.icheck.com.vn/app/locations/%s?geo=%s&page=%s&page_size=%s&keyword=%s", productCode, "21.735235,121.850293", currentCount, pageSize, searchKey)
        viewModel.loadSalePoint(requestSalePoint)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productCode = arguments?.getString(Const.TransferKey.EXTRA_ID) ?: ""
    }

    private fun populateData(data: List<IcheckSalePoint>) {
        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    private fun finishLoading() {
        swipe.isRefreshing = false
    }

}