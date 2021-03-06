package ishopgo.com.exhibition.ui.main.product.branded

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.BrandProductsRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.domain.response.SearchProducts
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ProductsOfBrandFragment : BaseListFragment<SearchProducts, Product>() {
    override fun initLoading() {
        firstLoad()
    }

    private var brandId: Long = -1L

    companion object {
        fun newInstance(params: Bundle): ProductsOfBrandFragment {
            val f = ProductsOfBrandFragment()
            f.arguments = params

            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        brandId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L

    }

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: SearchProducts) {
        if (reloadData) {
            val products = data.data ?: listOf()
            if (products.isEmpty()) {
                view_empty_result_notice.visibility = View.VISIBLE
                view_empty_result_notice.text = "Nội dung trống"
            } else view_empty_result_notice.visibility = View.GONE

            adapter.replaceAll(products)
            view_recyclerview.scheduleLayoutAnimation()
        } else {
            adapter.addAll(data.data ?: listOf())
        }
    }

    override fun itemAdapter(): BaseRecyclerViewAdapter<Product> {
        return ProductAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val loadMore = BrandProductsRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = 0
        loadMore.brandId = brandId
        viewModel.loadData(loadMore)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = BrandProductsRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.brandId = brandId
        viewModel.loadData(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<Product>) {
            (adapter as ClickableAdapter<Product>).listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                override fun click(position: Int, data: Product, code: Int) {
                    context?.let {
                        val intent = Intent(it, ProductDetailActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                        startActivity(intent)
                    }
                }
            }
        }
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.grid_layout_animation_from_bottom)

    }

    override fun obtainViewModel(): BaseListViewModel<SearchProducts> {
        return obtainViewModel(ProductsOfBrandViewModel::class.java, false)
    }
}
