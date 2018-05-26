package ishopgo.com.exhibition.ui.main.home.search.community

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.SearchCommunityRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.community.Community
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.community.CommunityProvider
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.home.search.SearchViewModel
import ishopgo.com.exhibition.ui.main.home.search.community.SearchCommunityAdapter.Companion.COMMUNITY_CLICK
import ishopgo.com.exhibition.ui.main.home.search.community.detail.CommunityResultDetailActivity
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

class CommunityResultFragment : BaseListFragment<List<CommunityProvider>, CommunityProvider>() {
    companion object {
        private val TAG = "CommunityResultFragment"
        const val COMMUNITY_CLICK = 1
    }

    private lateinit var sharedViewModel: SearchViewModel
    private var keyword = ""
    private var last_id: Long = 0
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

        (viewModel as SearchCommunityViewModel).total.observe(this, Observer { p ->
            p.let {
                if (it != null)
                    total = it
            }
        })
    }

    override fun populateData(data: List<CommunityProvider>) {
        if (data.isNotEmpty()) {
            val community = data[data.size - 1]
            if (community is Community)
                last_id = community.id
        }

        if (reloadData) {
            adapter.replaceAll(data)
            val community = Community()
            community.id = total.toLong()
            adapter.addData(0, community)
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
        if (adapter is ClickableAdapter<CommunityProvider>) {
            (adapter as ClickableAdapter<CommunityProvider>).listener = object : ClickableAdapter.BaseAdapterAction<CommunityProvider> {
                override fun click(position: Int, data: CommunityProvider, code: Int) {
                    when (code) {
                        COMMUNITY_CLICK -> {
                            if (data is Community) {
                                val intent = Intent(context, CommunityResultDetailActivity::class.java)
                                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                                startActivity(intent)
                            }
                        }
                        else -> {
                            if (data is Community) {
                                val intent = Intent(context, CommunityResultDetailActivity::class.java)
                                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }
}