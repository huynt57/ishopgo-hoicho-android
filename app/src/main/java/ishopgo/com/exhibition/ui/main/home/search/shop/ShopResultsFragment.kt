package ishopgo.com.exhibition.ui.main.home.search.shop

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchProductRequest
import ishopgo.com.exhibition.domain.request.SearchShopRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.home.search.SearchViewModel
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class ShopResultsFragment : BaseListFragment<List<SearchShopResultProvider>, SearchShopResultProvider>() {
    override fun initLoading() {
        firstLoad()
    }

    companion object {
        private val TAG = "ShopResultsFragment"
    }

    private var keyword = ""
    private lateinit var sharedViewModel: SearchViewModel
    private var total: Int = 0

    private fun search(key: String) {
        Log.d(TAG, "search: key = [${key}]")
        if (keyword == key)
            return
        else {
            keyword = key
            firstLoad()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_search_swipable_recyclerview, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedViewModel = obtainViewModel(SearchViewModel::class.java, true)
        sharedViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        sharedViewModel.searchKey.observe(this, Observer { k ->
            k?.let {
                search(it)
            }
        })

        (viewModel as SearchShopsViewModel).total.observe(this, Observer { p ->
            p.let {
                total = it ?: 0
                search_total.visibility = View.VISIBLE
                search_total.text = "$total kết quả được tìm thấy"
            }
        })
    }

    override fun populateData(data: List<SearchShopResultProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        }
        else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<SearchShopResultProvider> {
        val searchShopAdapter = SearchShopAdapter()
        searchShopAdapter.listener = object: ClickableAdapter.BaseAdapterAction<SearchShopResultProvider> {
            override fun click(position: Int, data: SearchShopResultProvider, code: Int) {
                context?.let {
                    if (data is IdentityData) {
                        val boothId = data.id
                        val intent = Intent(context, ShopDetailActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, boothId)
                        startActivity(intent)
                    }
                }
            }

        }
        return searchShopAdapter
    }

    override fun firstLoad() {
        super.firstLoad()
        val request = SearchShopRequest()
        request.keyword = keyword
        request.offset = 0
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val request = SearchShopRequest()
        request.keyword = keyword
        request.offset = currentCount
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
    }

    override fun obtainViewModel(): BaseListViewModel<List<SearchShopResultProvider>> {
        return obtainViewModel(SearchShopsViewModel::class.java, false)
    }


}