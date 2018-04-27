package ishopgo.com.exhibition.ui.base.list

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 3/28/18. HappyCoding!
 */
abstract class BaseListFragment<DATA, ITEM> : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    protected lateinit var adapter: BaseRecyclerViewAdapter<ITEM>
    protected lateinit var viewModel: BaseListViewModel<DATA>
    protected lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_swipable_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = itemAdapter()
        view_recyclerview.adapter = adapter
        view_recyclerview.setHasFixedSize(true)
        val layoutManager = layoutManager(view.context)
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

        viewModel = obtainViewModel()
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

    override fun onRefresh() {
        firstLoad()

    }

    open fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    abstract fun populateData(data: DATA)

    abstract fun itemAdapter(): BaseRecyclerViewAdapter<ITEM>

    open fun firstLoad() {
        reloadData = true
        swipe.isRefreshing = true
        adapter.clear()
        scrollListener.resetState()
    }

    open fun loadMore(currentCount: Int) {
        reloadData = false
        swipe.isRefreshing = true
    }

    open fun finishLoading() {
        swipe.isRefreshing = false
    }

    abstract fun obtainViewModel(): BaseListViewModel<DATA>

}