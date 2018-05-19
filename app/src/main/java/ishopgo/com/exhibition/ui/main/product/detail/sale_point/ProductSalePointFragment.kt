package ishopgo.com.exhibition.ui.main.product.detail.sale_point

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ProductSalePointRequest
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.ProductSalePoint
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductSalePointProvider
import ishopgo.com.exhibition.ui.main.product.detail.ProductSalePointAdapter
import ishopgo.com.exhibition.ui.main.product.detail.add_sale_point.ProductSalePointAddActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.Toolbox
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*

class ProductSalePointFragment : BaseListFragment<List<ProductSalePointProvider>, ProductSalePointProvider>() {
    private lateinit var data: ProductDetail

    override fun firstLoad() {
        super.firstLoad()
        val firstLoad = ProductSalePointRequest()
        firstLoad.limit = Const.PAGE_LIMIT
        firstLoad.offset = 0
        firstLoad.productId = data.id
        viewModel.loadData(firstLoad)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = ProductSalePointRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.productId = data.id
        viewModel.loadData(loadMore)
    }

    override fun populateData(data: List<ProductSalePointProvider>) {
        if (reloadData) {
            adapter.replaceAll(data)
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.getDefaultGson().fromJson(json, ProductDetail::class.java)
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<ProductSalePointProvider> {
        val adapter = ProductSalePointAdapter()
        adapter.addData(ProductSalePoint())
        return adapter
    }

    override fun obtainViewModel(): BaseListViewModel<List<ProductSalePointProvider>> {
        return obtainViewModel(ProductSalePointViewModel::class.java, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view_recyclerview.context, R.anim.linear_layout_animation_from_bottom)
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
    }

    fun openAddSalePoint() {
        val intent = Intent(context, ProductSalePointAddActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.getDefaultGson().toJson(data))
        startActivityForResult(intent, Const.RequestCode.SALE_POINT_ADD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.SALE_POINT_ADD && resultCode == Activity.RESULT_OK) {
            firstLoad()
            activity?.setResult(Activity.RESULT_OK)
        }
    }

    companion object {
        const val TAG = "ProductSalePointFragment"

        fun newInstance(params: Bundle): ProductSalePointFragment {
            val fragment = ProductSalePointFragment()
            fragment.arguments = params

            return fragment
        }
    }
}