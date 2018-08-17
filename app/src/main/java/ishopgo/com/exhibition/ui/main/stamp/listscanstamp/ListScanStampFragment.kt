package ishopgo.com.exhibition.ui.main.stamp.listscanstamp

import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.StampManager
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.stamp.stampmanager.StampManagerAdapter
import ishopgo.com.exhibition.ui.main.stamp.stampmanager.StampManagerViewModel
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class ListScanStampFragment : BaseListFragment<List<StampManager>, StampManager>() {
    override fun initLoading() {
        firstLoad()
    }

    override fun populateData(data: List<StampManager>) {
        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<StampManager> {
        return StampManagerAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<StampManager>> {
        return obtainViewModel(StampManagerViewModel::class.java, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        val request = LoadMoreRequest()
        request.offset = 0
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val request = LoadMoreRequest()
        request.offset = currentCount
        request.limit = Const.PAGE_LIMIT
        viewModel.loadData(request)
    }

    companion object {

        fun newInstance(params: Bundle): ListScanStampFragment {
            val fragment = ListScanStampFragment()
            fragment.arguments = params

            return fragment
        }
    }
}