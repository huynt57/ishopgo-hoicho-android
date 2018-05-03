package ishopgo.com.exhibition.ui.main.home.category.product

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_product_by_category.*

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class ProductsByCategoryFragment : BaseActionBarFragment() {

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_product_by_category
    }

    companion object {
        const val TAG = "ProductsByCategoryFragment"

        fun newInstance(params: Bundle): ProductsByCategoryFragment {
            val fragment = ProductsByCategoryFragment()
            fragment.arguments = params

            return fragment
        }

    }

    private val childAdapter = CategoryChildAdapter()
    private val productAdapter = ProductAdapter()
    protected lateinit var scrollListener: EndlessRecyclerViewScrollListener

    private lateinit var viewModel: ProductsByCategoryViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        setupChildCategories(view)

        setupProducts(view)

        setupBreadCrumb()

        swipe.setOnRefreshListener { firstLoad() }
    }

    private fun setupBreadCrumb() {
        context?.let {
            val item = LayoutInflater.from(it)
                    .inflate(R.layout.item_breadcrumb, view_breadcrumb_container, true) as LinearLayout
            item.tag = "item"
            val textView = item.getChildAt(0) as TextView
            textView.text = "item 1"
            textView.setOnClickListener { }


            val item2 = LayoutInflater.from(it)
                    .inflate(R.layout.item_breadcrumb, view_breadcrumb_container, true) as LinearLayout
            item2.tag = "item 1"
            val textView1 = item2.getChildAt(1) as TextView
            textView1.text = "item 2"
            textView1.setOnClickListener { }
        }
        view_breadcrumb_container

    }

    private fun setupProducts(view: View) {
        view_recyclerview.adapter = productAdapter
        view_recyclerview.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false)
        view_recyclerview.layoutManager = layoutManager
        view_recyclerview.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        view_recyclerview.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.grid_layout_animation_from_bottom)

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMore(totalItemsCount)
            }
        }
        view_recyclerview.addOnScrollListener(scrollListener)
    }

    private fun setupChildCategories(view: View) {
        view_child_categories.adapter = childAdapter
        view_child_categories.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view_child_categories.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
        childAdapter.listener = object : ClickableAdapter.BaseAdapterAction<CategoryProvider> {
            override fun click(position: Int, data: CategoryProvider, code: Int) {

            }

        }
    }

    private fun setupToolbar() {
        toolbar.setCustomTitle("Danh sách sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ProductsByCategoryViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.childCategories.observe(this, Observer { c ->
            c?.let {
                childAdapter.replaceAll(it)
            }
        })
        viewModel.products.observe(this, Observer { p ->
            p?.let {
                if (reloadData) {
                    productAdapter.replaceAll(it)
                    view_recyclerview.scheduleLayoutAnimation()
                }
                else
                    productAdapter.addAll(it)

                finishLoading()
            }
        })

        viewModel.loadChildCategory(0)

        firstLoad()
    }

    private fun firstLoad() {
        reloadData = true
        swipe.isRefreshing = true
        productAdapter.clear()
        scrollListener.resetState()

        viewModel.loadProductsByCategory(0)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false
        swipe.isRefreshing = true

        viewModel.loadProductsByCategory(currentCount.toLong())
    }

    private fun finishLoading() {
        swipe.isRefreshing = false
    }

}