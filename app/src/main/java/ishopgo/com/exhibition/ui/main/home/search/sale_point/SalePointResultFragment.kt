package ishopgo.com.exhibition.ui.main.home.search.sale_point

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchSalePointRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.home.search.SearchViewModel
import ishopgo.com.exhibition.ui.main.salepointdetail.SalePointDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

class SalePointResultFragment : BaseListFragment<List<SearchSalePoint>, SearchSalePoint>() {
    companion object {
        private val TAG = "SalePointResultFragment"
    }

    private lateinit var sharedViewModel: SearchViewModel
    private var keyword = ""
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedViewModel = obtainViewModel(SearchViewModel::class.java, true)
        sharedViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        sharedViewModel.searchKey.observe(this, Observer { k ->
            k?.let {
                search(it)
            }
        })

        (viewModel as SearchSalePointViewModel).total.observe(this, Observer { p ->
            p.let {
                if (it != null)
                    total = it
            }
        })
    }

    override fun populateData(data: List<SearchSalePoint>) {
        if (reloadData) {
            adapter.replaceAll(data)
            val salePoint = SearchSalePoint()
            salePoint.id = total.toLong()
            adapter.addData(0, salePoint)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<SearchSalePoint> {
        return SearchSalePointAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<SearchSalePoint>> {
        return obtainViewModel(SearchSalePointViewModel::class.java, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        val request = SearchSalePointRequest()
        request.keyword = keyword
        request.offset = 0
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val request = SearchSalePointRequest()
        request.keyword = keyword
        request.offset = currentCount
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))

        if (adapter is ClickableAdapter<SearchSalePoint>) {
            (adapter as ClickableAdapter<SearchSalePoint>).listener = object : ClickableAdapter.BaseAdapterAction<SearchSalePoint> {
                override fun click(position: Int, data: SearchSalePoint, code: Int) {
                    context?.let {
                        val intent = Intent(it, SalePointDetailActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, data.phone)
                        startActivity(intent)
                    }

                }
            }
        }
    }
}