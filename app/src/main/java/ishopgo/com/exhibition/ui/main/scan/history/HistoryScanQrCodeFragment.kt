package ishopgo.com.exhibition.ui.main.scan.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.HistoryScan
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_history_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class HistoryScanQrCodeFragment : BaseFragment() {
    private val adapter = HistoryQrCodeAdapter()

    companion object {
        fun newInstance(params: Bundle): HistoryScanQrCodeFragment {
            val fragment = HistoryScanQrCodeFragment()
            fragment.arguments = params

            return fragment
        }

        const val CLICK_PRODUCT = 0
        const val CLICK_LINK = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_history_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listQrCode = Toolbox.gson.fromJson<MutableList<HistoryScan>>(UserDataManager.currentQrCode, object : TypeToken<MutableList<HistoryScan>>() {}.type)
        if (listQrCode == null || listQrCode.isEmpty()) {
            view_empty_result_notice.visibility = View.VISIBLE
            view_empty_result_notice.text = "Nội dung trống"
        } else {
            view_empty_result_notice.visibility = View.GONE
            adapter.replaceAll(listQrCode)
        }

        adapter.listener = object : ClickableAdapter.BaseAdapterAction<HistoryScan> {
            override fun click(position: Int, data: HistoryScan, code: Int) {
                when (code) {
                    CLICK_PRODUCT -> {
                        context?.let {
                            val intent = Intent(it, ProductDetailActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_ID, data.productId)
                            startActivity(intent)
                        }
                    }
                    CLICK_LINK -> {
                        if (data.link.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.link))
                            if (context != null && intent.resolveActivity(context!!.packageManager) != null)
                                startActivity(intent)
                        } else toast("Có lỗi xảy ra")
                    }
                }

            }
        }


        view_recyclerview.adapter = adapter
        view_recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.isNestedScrollingEnabled = false
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
    }
}