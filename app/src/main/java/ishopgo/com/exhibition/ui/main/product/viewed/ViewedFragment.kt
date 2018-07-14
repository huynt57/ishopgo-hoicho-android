package ishopgo.com.exhibition.ui.main.product.viewed

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.FilterProductRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.FilterProduct
import ishopgo.com.exhibition.ui.base.list.BaseListFragment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.filterproduct.FilterProductViewModel
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*

/**
 * Created by xuanhong on 4/21/18. HappyCoding!
 */
class ViewedFragment : BaseListFragment<List<Product>, Product>() {
    override fun initLoading() {
        // do nothing, we start with default filter
    }

    private lateinit var filterViewModel: FilterProductViewModel
    private var filterProduct = FilterProduct()

    companion object {
        const val TAG = "ViewedFragment"
        fun newInstance(params: Bundle): ViewedFragment {
            val fragment = ViewedFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun layoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    @SuppressLint("SetTextI18n")
    override fun populateData(data: List<Product>) {
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

    override fun itemAdapter(): BaseRecyclerViewAdapter<Product> {
        return ProductAdapter()
    }

    override fun firstLoad() {
        super.firstLoad()
        val loadMore = FilterProductRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = 0
        loadMore.sort_by = filterProduct.sort_by ?: "name"
        loadMore.sort_type = filterProduct.sort_type ?: "asc"
        loadMore.type_filter = filterProduct.filter ?: mutableListOf()
        viewModel.loadData(loadMore)
    }

    override fun loadMore(currentCount: Int) {
        super.loadMore(currentCount)
        val loadMore = FilterProductRequest()
        loadMore.limit = Const.PAGE_LIMIT
        loadMore.offset = currentCount
        loadMore.sort_by = filterProduct.sort_by ?: "name"
        loadMore.sort_type = filterProduct.sort_type ?: "asc"
        loadMore.type_filter = filterProduct.filter ?: mutableListOf()
        viewModel.loadData(loadMore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        if (adapter is ClickableAdapter<Product>)
            (adapter as ClickableAdapter<Product>).listener = object : ClickableAdapter.BaseAdapterAction<Product> {
                override fun click(position: Int, data: Product, code: Int) {
                    context?.let {
                        val intent = Intent(it, ProductDetailActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                        startActivity(intent)
                    }
                }
            }
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.grid_layout_animation_from_bottom)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        filterViewModel = obtainViewModel(FilterProductViewModel::class.java, true)
        filterViewModel.getDataFilter.observe(this, Observer { p ->
            p?.let {
                filterProduct = it
                firstLoad()
            }
        })

        // default loading
        if (filterViewModel.getDataFilter.value == null) {
            filterViewModel.getDataFilter(FilterProduct())
        }
    }

    override fun obtainViewModel(): BaseListViewModel<List<Product>> {
        return obtainViewModel(ViewedProductsViewModel::class.java, false)
    }
}
