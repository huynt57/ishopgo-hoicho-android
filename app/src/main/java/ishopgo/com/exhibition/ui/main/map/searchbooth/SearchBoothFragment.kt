package ishopgo.com.exhibition.ui.main.map.searchbooth

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ExpoShopLocationRequest
import ishopgo.com.exhibition.domain.response.Kiosk
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.login.LoginActivity
import ishopgo.com.exhibition.ui.main.map.ExpoDetailViewModel
import ishopgo.com.exhibition.ui.main.map.ExpoShopAdapter
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 6/19/18. HappyCoding!
 */
class SearchBoothFragment : BaseSearchActionBarFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun openFilter() {

    }

    override fun showFilter(): Boolean {
        return false
    }

    companion object {
        const val TAG = "SearchBoothFragment"

        fun newInstance(arg: Bundle): SearchBoothFragment {
            val f = SearchBoothFragment()
            f.arguments = arg
            return f
        }
    }

    private var searchKey = ""
    private lateinit var adapter: ExpoShopAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var viewModel: ExpoDetailViewModel
    private var expoId: Long = -1L

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

    private fun openShopDetail(shopId: Long) {
        val intent = Intent(context, ShopDetailActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, shopId)
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchField().hint = "Tìm kiếm gian hàng"
        search_total.visibility = View.GONE

        adapter = ExpoShopAdapter()
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<Kiosk> {
            override fun click(position: Int, data: Kiosk, code: Int) {
                if (data.boothId != null && data.boothId != 0L) {
                    openShopDetail(data.boothId!!)
                } else {
                    chooseKiosk(data)
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

    private fun chooseKiosk(data: Kiosk) {
        if (UserDataManager.currentUserId > 0) {
            if (UserDataManager.currentType == "Chủ hội chợ") {
                val extra = Bundle()
                extra.putLong(Const.TransferKey.EXTRA_ID, data.id ?: -1L)
                Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_searchBoothFragment_to_chooseBoothFragment, extra)
            } else {
                Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_searchBoothFragment_to_registerBoothFragmentActionBar)
            }
        } else {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
            startActivity(intent)
        }
    }

    private fun chooseShop(data: Kiosk) {
        val extra = Bundle()
        extra.putLong(Const.TransferKey.EXTRA_ID, data.id ?: -1L)
        Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_searchBoothFragment_to_chooseBoothFragment, extra)
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

        firstLoad()
    }

    private fun firstLoad() {
        reloadData = true
        adapter.clear()
        scrollListener.resetState()

        val request = ExpoShopLocationRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = 0
        request.searchKeyword = searchKey
        request.expoId = expoId
        viewModel.loadData(request)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false

        val request = ExpoShopLocationRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = currentCount
        request.searchKeyword = searchKey
        request.expoId = expoId
        viewModel.loadData(request)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(ExpoDetailViewModel::class.java, false)

        expoId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    private fun populateData(data: List<Kiosk>) {
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