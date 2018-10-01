package ishopgo.com.exhibition.ui.main.product.detail.exchange_diary

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ListBGBNRequest
import ishopgo.com.exhibition.domain.response.ListBGBN
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.hideKeyboard
import ishopgo.com.exhibition.ui.main.product.detail.ExchangeDiaryProductViewModel
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailViewModel
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

class ProductExchangeBGBNFragment : BaseSearchActionBarFragment(), BackpressConsumable, SwipeRefreshLayout.OnRefreshListener {
    companion object {
        const val TAG = "ProductExchangeBGBNFragment"

        fun newInstance(arg: Bundle): ProductExchangeBGBNFragment {
            val f = ProductExchangeBGBNFragment()
            f.arguments = arg
            return f
        }

    }

    private var searchKey = ""
    private lateinit var adapter: BGBNAdapter
    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var shareViewModel: ExchangeDiaryProductViewModel
    private var data = ProductDetail()
    var BEN_GUI = true

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, ProductDetail::class.java)

        BEN_GUI = arguments?.getBoolean(Const.TransferKey.EXTRA_STATUS_BG_BN) ?: true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchField().hint = if (BEN_GUI) "Tìm kiếm bên giao" else "Tìm kiếm bên nhận"
        search_total.visibility = View.GONE

        adapter = BGBNAdapter()
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<ListBGBN> {
            override fun click(position: Int, data: ListBGBN, code: Int) {
                if (BEN_GUI) shareViewModel.selectedBenGiao(data) else shareViewModel.selectedBenNhan(data)
                activity?.onBackPressed()
            }

        }
        view_recyclerview.adapter = adapter
        view_recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager

        swipe.setOnRefreshListener(this)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        shareViewModel = obtainViewModel(ExchangeDiaryProductViewModel::class.java, true)
        viewModel = obtainViewModel(ProductDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.getListBGBN.observe(this, Observer { data ->
            data?.let {
                populateData(it)

                finishLoading()
            }
        })

        firstLoad()
    }

    private fun populateData(data: List<ListBGBN>) {
        adapter.replaceAll(data)
        view_recyclerview.scheduleLayoutAnimation()
    }

    private fun finishLoading() {
        swipe.isRefreshing = false
    }

    private fun firstLoad() {
        val firstLoad = ListBGBNRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.product_id = data.id
        firstLoad.booth_id = data.booth?.id ?: -1L
        firstLoad.keyword = searchKey
        viewModel.getListBGBN(firstLoad)
    }
}