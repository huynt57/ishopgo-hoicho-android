package ishopgo.com.exhibition.ui.community.notification

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.Notification
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.notification.NotificationAdapter
import ishopgo.com.exhibition.ui.main.notification.NotificationProvider
import ishopgo.com.exhibition.ui.main.notification.NotificationViewModel
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class CommunityNotificationFragment : BaseListFragment<List<NotificationProvider>, NotificationProvider>() {
    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<NotificationProvider>) {
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
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<NotificationProvider> {
        val adapter = NotificationAdapter()
        adapter.addData(Notification())
        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<NotificationProvider>> {
        return obtainViewModel(NotificationViewModel::class.java, false)
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

    fun marksAllAsRead() {
        if (viewModel is NotificationViewModel) {
            (viewModel as NotificationViewModel).marksAllAsRead()
        }
        showProgressDialog()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel is NotificationViewModel) {
            (viewModel as NotificationViewModel).marksAllSuccess.observe(this, Observer {
                hideProgressDialog()
                firstLoad()
            })

            (viewModel as NotificationViewModel).markNotificationSuccess.observe(this, Observer {
                toast("Đang phát triển")
//            val intent = Intent(context, NotificationDetailActivity::class.java)
//            startActivityForResult(intent, Const.RequestCode.NOTIFICATION_DETAIL)
            })
        }
    }

    companion object {
        const val TAG = "CommunityNotificationFragment"

        fun newInstance(data: Bundle): CommunityNotificationFragment {
            val fragment = CommunityNotificationFragment()
            fragment.arguments = data

            return fragment
        }
    }
}