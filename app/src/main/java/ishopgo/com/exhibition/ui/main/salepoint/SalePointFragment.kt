package ishopgo.com.exhibition.ui.main.salepoint

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.SalePoint
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.salepoint.add.SalePointAddActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class SalePointFragment : BaseListFragment<List<SalePoint>, SalePoint>() {

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

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<SalePoint>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<SalePoint> {
        val adapter = SalePointAdapter()
        adapter.addData(SalePoint())
        adapter.listener = object : ClickableAdapter.BaseAdapterAction<SalePoint> {
            override fun click(position: Int, data: SalePoint, code: Int) {
                if (viewModel is SalePointViewModel) {
                    (viewModel as SalePointViewModel).changeStatusSalePoint(data.id)
                }
            }
        }
        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<SalePoint>> {
        return obtainViewModel(SalePointViewModel::class.java, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
    }

    fun openAddSalePoint() {
        val intent = Intent(context, SalePointAddActivity::class.java)
        startActivityForResult(intent, Const.RequestCode.SALE_POINT_ADD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.SALE_POINT_ADD && resultCode == Activity.RESULT_OK) {
            firstLoad()
        }
    }

    companion object {
        const val TAG = "SalePointFragment"

        fun newInstance(params: Bundle): SalePointFragment {
            val fragment = SalePointFragment()
            fragment.arguments = params

            return fragment
        }
    }
}