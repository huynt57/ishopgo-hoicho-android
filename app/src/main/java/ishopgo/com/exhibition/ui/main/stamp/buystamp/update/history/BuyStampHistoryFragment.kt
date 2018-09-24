package ishopgo.com.exhibition.ui.main.stamp.buystamp.update.history

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.StampListBuy
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class BuyStampHistoryFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        swipe.isRefreshing = false
    }

    private var data: StampListBuy? = null
    private val adapter = BuyStampHistoryAdapter()

    companion object {
        const val TAG = "BuyStampHistoryFragment"
        fun newInstance(params: Bundle): BuyStampHistoryFragment {
            val fragment = BuyStampHistoryFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, StampListBuy::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_swipable_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (data == null || data?.histories?.isEmpty() == true) {
            view_empty_result_notice.visibility = View.VISIBLE
            view_empty_result_notice.text = "Nội dung trống"
        } else {
            view_empty_result_notice.visibility = View.GONE
            adapter.replaceAll(data!!.histories ?: mutableListOf())
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