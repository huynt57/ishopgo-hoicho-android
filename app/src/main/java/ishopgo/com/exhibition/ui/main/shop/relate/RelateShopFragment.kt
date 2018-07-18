package ishopgo.com.exhibition.ui.main.shop.relate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ShopRelateRequest
import ishopgo.com.exhibition.domain.response.BoothRelate
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class RelateShopFragment : BaseListFragment<List<BoothRelate>, BoothRelate>() {
    override fun initLoading() {
        firstLoad()
    }

    companion object {
        fun newInstance(params: Bundle): RelateShopFragment {
            val fragment = RelateShopFragment()
            fragment.arguments = params
            return fragment
        }
    }

    private var shopId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shopId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<BoothRelate>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<BoothRelate> {
        return RelateShopAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val loadMore = ShopRelateRequest()
        loadMore.shopId = shopId
        viewModel.loadData(loadMore)
    }

    override fun loadMore(currentCount: Int) {
//        super.loadMore(currentCount)
//        val loadMore = ShopRelateRequest()
//        loadMore.limit = Const.PAGE_LIMIT
//        loadMore.offset = currentCount
//        loadMore.shopId = shopId
//        viewModel.loadData(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.linear_layout_animation_from_bottom)

        if (adapter is ClickableAdapter<BoothRelate>) {
            (adapter as ClickableAdapter<BoothRelate>).listener = object : ClickableAdapter.BaseAdapterAction<BoothRelate> {
                override fun click(position: Int, data: BoothRelate, code: Int) {
//                    val extra = Bundle()
//                    extra.putLong(Const.TransferKey.EXTRA_ID, data.id)
//
//                    Navigat
                    val intent = Intent(view.context, ShopDetailActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                    startActivity(intent)
                }
            }
        }
    }

    override fun obtainViewModel(): BaseListViewModel<List<BoothRelate>> {
        return obtainViewModel(RelateShopViewModel::class.java, false)
    }

}