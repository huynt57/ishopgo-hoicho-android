package ishopgo.com.exhibition.ui.main.stamp.stampdistribution

import android.content.Intent
import android.os.Bundle
import android.view.View
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.StampDistribution
import ishopgo.com.exhibition.domain.response.StampManager
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.stamp.nostamp.assign.NoStampAssignActivity
import ishopgo.com.exhibition.ui.main.stamp.stampmanager.StampManagerAdapter
import ishopgo.com.exhibition.ui.main.stamp.stampmanager.StampManagerViewModel
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class StampDistributionFragment : BaseListFragment<List<StampDistribution>, StampDistribution>() {
    override fun initLoading() {
        firstLoad()
    }

    override fun populateData(data: List<StampDistribution>) {
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

    fun openAssignNoStamp() {
        val intent = Intent(context, NoStampAssignActivity::class.java)
//        intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
//        intent.putExtra(Const.TransferKey.EXTRA_STAMP_COUNT, data.quantityExists)
        startActivityForResult(intent, Const.RequestCode.ASSIGN_NO_STAMP)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<StampDistribution> {
        return StampDistributionAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<StampDistribution>> {
        return obtainViewModel(StampDistributionViewModel::class.java, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (adapter as StampDistributionAdapter).listener = object : ClickableAdapter.BaseAdapterAction<StampDistribution> {
            override fun click(position: Int, data: StampDistribution, code: Int) {
                toast("Đang phát triển")
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
        const val TAG = "StampDistributionFragment"
        fun newInstance(params: Bundle): StampDistributionFragment {
            val fragment = StampDistributionFragment()
            fragment.arguments = params

            return fragment
        }
    }
}