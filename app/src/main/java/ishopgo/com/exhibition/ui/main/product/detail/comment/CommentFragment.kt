package ishopgo.com.exhibition.ui.main.product.detail.comment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ProductCommentsRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.domain.response.ProductComment
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class CommentFragment : BaseListFragment<List<ProductComment>, ProductComment>() {

    companion object {
        fun newInstance(params: Bundle): CommentFragment {
            val f = CommentFragment()
            f.arguments = params

            return f
        }
    }

    private var productId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<ProductComment>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<ProductComment> {
        return ProductCommentAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val loadMore = ProductCommentsRequest()
        loadMore.lastId = -1L
        loadMore.parentId = -1L
        loadMore.productId = productId
        loadMore.limit = Const.PAGE_LIMIT
        viewModel.loadData(loadMore)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = ProductCommentsRequest()
        val item = adapter.getItem(adapter.itemCount - 1)
            loadMore.lastId = item.id
            loadMore.parentId = -1L
            loadMore.productId = productId
            loadMore.limit = Const.PAGE_LIMIT
            viewModel.loadData(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
    }

    override fun obtainViewModel(): BaseListViewModel<List<ProductComment>> {
        return obtainViewModel(ProductCommentViewModel::class.java, false)
    }

}