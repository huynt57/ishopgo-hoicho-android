package ishopgo.com.exhibition.ui.main.stamp.buystamp

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.StampListBuy
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.stamp.buystamp.add.BuyStampAddActivity
import ishopgo.com.exhibition.ui.main.stamp.buystamp.update.BuyStampUpdatedActivity
import kotlinx.android.synthetic.main.content_stamp_orders.*
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class BuyStampFragment : BaseListFragment<List<StampListBuy>, StampListBuy>() {
    override fun initLoading() {
        firstLoad()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_stamp_orders, container, false)
    }

    override fun populateData(data: List<StampListBuy>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<StampListBuy> {
        return StampListBuyAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<StampListBuy>> {
        return obtainViewModel(StampListBuyViewModel::class.java, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (adapter as StampListBuyAdapter).listener = object : ClickableAdapter.BaseAdapterAction<StampListBuy> {
            override fun click(position: Int, data: StampListBuy, code: Int) {
                val intent = Intent(context, BuyStampUpdatedActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                startActivityForResult(intent, Const.RequestCode.STAMP_ORDER_UPDATE)
            }
        }
    }

    fun openStampOrderAdd() {
        val intent = Intent(context, BuyStampAddActivity::class.java)
        startActivityForResult(intent, Const.RequestCode.STAMP_ORDER_ADD)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (viewModel as StampListBuyViewModel).statistical.observe(this, Observer { p ->
            p?.let {
                tv_soTemDaMua.text = "${it.stampTotal ?: 0}\nSố tem đã mua"
                tv_soTemDaKichHoat.text = "${it.stampActive ?: 0}\nSố tem đã kích hoạt"
                tv_soTemConTrong.text = "${it.stampEmpty ?: 0}\nSố tem còn trống"
                tv_soLoTem.text = "${it.stampDetailTotal ?: 0}\nSố lô tem"
                tv_soNguoiQuet.text = "${it.stampUserScan ?: 0}\nSố người quét"
                tv_soTemCanhBao.text = "${it.stampWarning ?: 0}\nSố tem cảnh báo"
            }
        })
        (viewModel as StampListBuyViewModel).getStampOrderStatistical()
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
        const val TAG = "BuyStampFragment"
        fun newInstance(params: Bundle): BuyStampFragment {
            val fragment = BuyStampFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == Const.RequestCode.STAMP_ORDER_UPDATE || requestCode == Const.RequestCode.STAMP_ORDER_ADD) && resultCode == Activity.RESULT_OK) {
            (viewModel as StampListBuyViewModel).getStampOrderStatistical()
            firstLoad()
        }
    }
}