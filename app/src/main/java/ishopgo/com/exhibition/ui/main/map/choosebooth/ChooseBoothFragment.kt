package ishopgo.com.exhibition.ui.main.map.choosebooth

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import androidx.navigation.Navigation
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchBoothRequest
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseSearchActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.map.ExpoDetailViewModel
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 6/19/18. HappyCoding!
 */
class ChooseBoothFragment : BaseSearchActionBarFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val TAG = "ChooseBoothFragment"

        fun newInstance(arg: Bundle): ChooseBoothFragment {
            val f = ChooseBoothFragment()
            f.arguments = arg
            return f
        }
    }

    private var searchKey = ""
    private lateinit var adapter: ExpoBoothAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var viewModel: ExpoDetailViewModel
    private var positionId: Long = -1L

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchField().hint = "Tìm gian hàng muốn thêm"
        search_total.visibility = View.GONE

        adapter = ExpoBoothAdapter()
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<BoothManager> {
            override fun click(position: Int, data: BoothManager, code: Int) {
                chooseBooth(data)
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

    private fun chooseBooth(data: BoothManager) {
        if (positionId != -1L)
            viewModel.assignBooth(positionId, data.id)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.availableBooths.observe(this, Observer { data ->
            data?.let {
                populateData(it)

                finishLoading()
            }
        })

        viewModel.boothAssigned.observe(this, Observer { i ->
            if (i == true) {
                toast("Gắn gian hàng thành công")
                Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.finish_chosing)
            }
        })

        firstLoad()
    }

    private fun firstLoad() {
        reloadData = true
        adapter.clear()
        scrollListener.resetState()

        val request = SearchBoothRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = 0
        request.keyword = searchKey
        viewModel.loadAvailableBooths(request)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false

        val request = SearchBoothRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = currentCount
        request.keyword = searchKey
        viewModel.loadAvailableBooths(request)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(ExpoDetailViewModel::class.java, false)

        positionId = arguments?.getLong(Const.TransferKey.EXTRA_ID) ?: -1L
    }

    private fun populateData(data: List<BoothManager>) {
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