package ishopgo.com.exhibition.ui.base.list

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 3/28/18. HappyCoding!
 */
abstract class BaseListFragment<DATA, ITEM> : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    protected lateinit var adapter: BaseListAdapter<ITEM>
    protected lateinit var viewModel: BaseListViewModel<DATA>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_swipable_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = itemAdapter()
        view_recyclerview.adapter = adapter
        view_recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager

        view_recyclerview.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                loadMore(totalItemsCount)
            }
        })

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

                swipe.isRefreshing = false
            }
        })

        firstLoad()
        swipe.isRefreshing = true
    }

    override fun onRefresh() {
        firstLoad()

        swipe.isRefreshing = true
    }

    abstract fun populateData(data: DATA)

    abstract fun itemAdapter(): BaseListAdapter<ITEM>

    abstract fun title(): String

    abstract fun firstLoad()

    abstract fun loadMore(currentCount: Int)

    abstract fun obtainViewModel(): BaseListViewModel<DATA>

}