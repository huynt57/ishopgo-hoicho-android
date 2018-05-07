package ishopgo.com.exhibition.ui.main.shop.rate

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ShopRatesRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class RateFragment : BaseListFragment<List<ShopRateProvider>, ShopRateProvider>() {

    companion object {
        fun newInstance(params: Bundle): RateFragment {
            val fragment = RateFragment()
            fragment.arguments = params
            return fragment
        }
    }

    private var shopId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shopId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun populateData(data: List<ShopRateProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else
            adapter.addAll(data)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<ShopRateProvider> {
        return RateAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val loadMore = ShopRatesRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = 0
        loadMore.shopId = shopId
        viewModel.loadData(loadMore)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = ShopRatesRequest()
        val item = adapter.getItem(adapter.itemCount - 1)
        if (item is IdentityData) {
            loadMore.limit = Const.PAGE_LIMIT
            loadMore.offset = currentCount
            loadMore.shopId = shopId
            viewModel.loadData(loadMore)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)
    }

    override fun obtainViewModel(): BaseListViewModel<List<ShopRateProvider>> {
        return obtainViewModel(RateViewModel::class.java, false)
    }

}