package ishopgo.com.exhibition.ui.main.home.search.community

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchCommunityRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.community.CommunityProvider
import ishopgo.com.exhibition.ui.main.home.search.SearchViewModel
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

class CommunityResultFragment : BaseListFragment<List<CommunityProvider>, CommunityProvider>() {
    companion object {
        private val TAG = "CommunityResultFragment"
    }

    private lateinit var sharedViewModel: SearchViewModel
    private var keyword = ""
    private var last_id: Long = 0

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
    }

    override fun populateData(data: List<CommunityProvider>) {
        if (data.isNotEmpty()) {
            last_id = data[data.size - 1].providerId()
        }

        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<CommunityProvider> {
        return SearchCommunityAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<CommunityProvider>> {
        return obtainViewModel(SearchCommunityViewModel::class.java, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        val request = SearchCommunityRequest()
        request.content = keyword
        request.last_id = 0
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val request = SearchCommunityRequest()
        request.content = keyword
        request.last_id = last_id
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
    }
}