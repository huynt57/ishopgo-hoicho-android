package ishopgo.com.exhibition.ui.main.references

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.member.MemberManager
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import kotlinx.android.synthetic.main.content_search_swipable_recyclerview.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

/**
 * Created by xuanhong on 7/18/18. HappyCoding!
 */
class ReferencesFragment : BaseListFragment<List<MemberManager>, MemberManager>() {
    override fun initLoading() {
        firstLoad()
    }

    override fun populateData(data: List<MemberManager>) {
        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data)
        }
        hideProgressDialog()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_search_swipable_recyclerview, container, false)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<MemberManager> {
        val visitorsAdapter = ReferencesAdapter()
        visitorsAdapter.listener = object : ClickableAdapter.BaseAdapterAction<MemberManager> {
            override fun click(position: Int, data: MemberManager, code: Int) {
                context?.let {
                    val intent = Intent(it, MemberProfileActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                    it.startActivity(intent)
                }
            }
        }

        return visitorsAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel is ReferencesViewModel) {
            val referencesViewModel = viewModel as ReferencesViewModel
            referencesViewModel.totalMember.observe(viewLifeCycleOwner!!, Observer {
                it?.let {
                    if (it > 0) search_total.visibility = View.VISIBLE
                    search_total.text = "Tổng số: $it người"
                }

            })
        }
    }

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = LoadMoreRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.loadData(loadMore)
    }

    override fun obtainViewModel(): BaseListViewModel<List<MemberManager>> {
        return obtainViewModel(ReferencesViewModel::class.java, false)
    }

}