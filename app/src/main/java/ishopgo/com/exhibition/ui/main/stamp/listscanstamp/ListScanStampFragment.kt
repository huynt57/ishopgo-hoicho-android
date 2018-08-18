package ishopgo.com.exhibition.ui.main.stamp.listscanstamp

import android.content.Intent
import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.StampManager
import ishopgo.com.exhibition.domain.response.StampUserListScan
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.stamp.listscanstamp.detail.ListScanStampDetailActivity
import ishopgo.com.exhibition.ui.main.stamp.stampmanager.StampManagerAdapter
import ishopgo.com.exhibition.ui.main.stamp.stampmanager.StampManagerViewModel
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class ListScanStampFragment : BaseListFragment<List<StampUserListScan>, StampUserListScan>() {
    override fun initLoading() {
        firstLoad()
    }

    override fun populateData(data: List<StampUserListScan>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<StampUserListScan> {
        return ListScanStampAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<StampUserListScan>> {
        return obtainViewModel(ListScanStampViewModel::class.java, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (adapter as ListScanStampAdapter).listener = object : ClickableAdapter.BaseAdapterAction<StampUserListScan> {
            override fun click(position: Int, data: StampUserListScan, code: Int) {
                context?.let {
                    val intent = Intent(context, ListScanStampDetailActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data.detail))
                    it.startActivity(intent)
                }
            }
        }
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