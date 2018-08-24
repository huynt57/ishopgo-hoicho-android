package ishopgo.com.exhibition.ui.main.product.icheckproduct.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.IcheckRequest
import ishopgo.com.exhibition.domain.response.IcheckProduct
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductActivity
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductAdapter
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

class IcheckShopProductFragment : BaseListFragment<List<IcheckProduct>, IcheckProduct>() {
    private var shopId = 0L

    override fun initLoading() {
        firstLoad()
    }

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    override fun populateData(data: List<IcheckProduct>) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            view_recyclerview.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.grid_layout_animation_from_bottom)

            (adapter as IcheckProductAdapter).listener = object : ClickableAdapter.BaseAdapterAction<IcheckProduct> {
                override fun click(position: Int, data: IcheckProduct, code: Int) {
                    context?.let {
                        val intent = Intent(it, IcheckProductActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                        it.startActivity(intent)
                    }
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<IcheckProduct> {
        return IcheckProductAdapter()
    }

    override fun obtainViewModel(): BaseListViewModel<List<IcheckProduct>> {
        return obtainViewModel(IcheckShopProductViewModel::class.java, false)
    }

    override fun firstLoad() {
        super.firstLoad()
        val param = IcheckRequest()
        val requestSalePoint = String.format("https://core.icheck.com.vn/vendors/%s/products?skip=%s&limit=%s", shopId, 0, Const.PAGE_LIMIT)
        param.param = requestSalePoint
        viewModel.loadData(param)
        view_recyclerview.scheduleLayoutAnimation()
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val param = IcheckRequest()
        val requestSalePoint = String.format("https://core.icheck.com.vn/vendors/%s/products?skip=%s&limit=%s", shopId, currentCount, Const.PAGE_LIMIT)
        param.param = requestSalePoint
        viewModel.loadData(param)
    }

    companion object {
        const val TAG = "IcheckShopProductFragment"
        fun newInstance(params: Bundle): IcheckShopProductFragment {
            val fragment = IcheckShopProductFragment()
            fragment.arguments = params

            return fragment
        }
    }
}
