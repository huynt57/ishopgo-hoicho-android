package ishopgo.com.exhibition.ui.main.notification

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.Notification
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.notification.add.NotificationAddActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by hoangnh on 5/7/2018.
 */
class NotificationFragment : BaseListFragment<List<NotificationProvider>, NotificationProvider>() {

    companion object {
        const val TAG = "NotificationAddFragment"
    }

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun populateData(data: List<NotificationProvider>) {
        if (reloadData) {
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
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = LoadMoreRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        viewModel.loadData(loadMore)
    }

    fun openNotificationAdd() {
        val intent = Intent(context, NotificationAddActivity::class.java)
        startActivityForResult(intent, Const.RequestCode.NOTIFICATION_ADD)
    }

    fun marksAllAsRead() {
        if (viewModel is NotificationViewModel) {
            (viewModel as NotificationViewModel).marksAllAsRead()
        }
        showProgressDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<NotificationProvider>) {
            (adapter as ClickableAdapter<NotificationProvider>).listener = object : ClickableAdapter.BaseAdapterAction<NotificationProvider> {
                override fun click(position: Int, data: NotificationProvider, code: Int) {
                    if (viewModel is NotificationViewModel) {
                        (viewModel as NotificationViewModel).markAsReadThenShowDetail(data.provideId())
                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel is NotificationViewModel) {
            (viewModel as NotificationViewModel).marksAllSuccess.observe(this, Observer {
                hideProgressDialog()
                firstLoad()
            })

            (viewModel as NotificationViewModel).markNotificationSuccess.observe(this, Observer {
                toast("Xem chi tiết thông báo")
//            val intent = Intent(context, NotificationDetailActivity::class.java)
//            startActivityForResult(intent, Const.RequestCode.NOTIFICATION_DETAIL)
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.NOTIFICATION_DETAIL && resultCode == Activity.RESULT_OK) {
            firstLoad()
        }
    }
}