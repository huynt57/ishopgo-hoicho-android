package ishopgo.com.exhibition.ui.main.product.icheckproduct.review

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.IcheckRequest
import ishopgo.com.exhibition.domain.response.IcheckReview
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckReviewAdapter
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class IcheckReviewFragment : BaseListFragment<List<IcheckReview>, IcheckReview>() {
    override fun initLoading() {
        firstLoad()
    }

    companion object {
        const val TAG = "IcheckReviewFragment"
        fun newInstance(params: Bundle): IcheckReviewFragment {
            val fragment = IcheckReviewFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private var productId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<IcheckReview>) {
        if (reloadData) {
            if (data.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(data)
        } else {
            adapter.addAll(data)
        }
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<IcheckReview> {
        return IcheckReviewAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val param = IcheckRequest()
        val page = 1
        val pageSize = Const.PAGE_LIMIT
        val requestSalePoint = String.format("https://core.icheck.com.vn/reviews?object_id=%s&limit=%s&skip=%s", productId, pageSize, page)
        param.param = requestSalePoint
        viewModel.loadData(param)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val param = IcheckRequest()
        val pageSize = Const.PAGE_LIMIT
        val requestSalePoint = String.format("https://core.icheck.com.vn/reviews?object_id=%s&limit=%s&skip=%s", productId, pageSize, currentCount)
        param.param = requestSalePoint
        viewModel.loadData(param)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
    }

    override fun obtainViewModel(): BaseListViewModel<List<IcheckReview>> {
        return obtainViewModel(IcheckReviewViewModel::class.java, false)
    }
}