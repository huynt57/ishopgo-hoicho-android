package ishopgo.com.exhibition.ui.main.shop.rate

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ProductCommentsRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentAdapter
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentProvider
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class RateFragment : BaseListFragment<List<ProductCommentProvider>, ProductCommentProvider>() {
    override fun populateData(data: List<ProductCommentProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<ProductCommentProvider> {
        return ProductCommentAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val loadMore = ProductCommentsRequest()
        loadMore.lastId = -1L
        loadMore.parentId = -1L
        loadMore.productId = 1
        viewModel.loadData(loadMore)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = ProductCommentsRequest()
        val item = adapter.getItem(adapter.itemCount)
        if (item is IdentityData) {
            loadMore.lastId = item.id
            loadMore.parentId = -1L
            loadMore.productId = 1
            viewModel.loadData(loadMore)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
    }

    override fun obtainViewModel(): BaseListViewModel<List<ProductCommentProvider>> {
        return obtainViewModel(RateViewModel::class.java, false)
    }

}