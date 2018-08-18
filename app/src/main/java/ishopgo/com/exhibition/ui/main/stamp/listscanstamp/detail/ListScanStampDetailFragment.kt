package ishopgo.com.exhibition.ui.main.stamp.listscanstamp.detail

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.StampManager
import ishopgo.com.exhibition.domain.response.StampUserListScan
import ishopgo.com.exhibition.domain.response.StampUserListScanDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.stamp.stampmanager.StampManagerAdapter
import ishopgo.com.exhibition.ui.main.stamp.stampmanager.StampManagerViewModel
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class ListScanStampDetailFragment : BaseFragment() {
    private val adapter = ListScanStampDetailAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_swipable_recyclerview, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = Toolbox.gson.fromJson<ArrayList<StampUserListScanDetail>>(arguments?.getString(Const.TransferKey.EXTRA_JSON),
                object : TypeToken<ArrayList<StampUserListScanDetail>>() {}.type)
        adapter.replaceAll(data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.adapter = adapter
        view_recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.isNestedScrollingEnabled = false
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
    }

    companion object {

        fun newInstance(params: Bundle): ListScanStampDetailFragment {
            val fragment = ListScanStampDetailFragment()
            fragment.arguments = params

            return fragment
        }
    }
}