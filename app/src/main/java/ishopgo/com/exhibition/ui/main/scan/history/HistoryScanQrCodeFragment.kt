package ishopgo.com.exhibition.ui.main.scan.history

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.HistoryScan
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class HistoryScanQrCodeFragment : BaseFragment() , SwipeRefreshLayout.OnRefreshListener {
    private val adapter = HistoryQrCodeAdapter()

    override fun onRefresh() {
        swipe.isRefreshing = false
    }
    companion object {
        fun newInstance(params: Bundle): HistoryScanQrCodeFragment {
            val fragment = HistoryScanQrCodeFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_swipable_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listQrCode = Toolbox.gson.fromJson<MutableList<HistoryScan.QrCode>>(UserDataManager.currentQrCode, object : TypeToken<MutableList<HistoryScan.QrCode>>() {}.type)
        if (listQrCode == null || listQrCode.isEmpty()) {
            view_empty_result_notice.visibility = View.VISIBLE
            view_empty_result_notice.text = "Nội dung trống"
        } else {
            view_empty_result_notice.visibility = View.GONE
            adapter.replaceAll(listQrCode)
        }


        view_recyclerview.adapter = adapter
        view_recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.isNestedScrollingEnabled = false
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))

        swipe.setOnRefreshListener(this)
        swipe.isRefreshing = false
    }
}