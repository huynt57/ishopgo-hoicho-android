package ishopgo.com.exhibition.ui.main.home.category.product

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.CategoriedProductsRequest
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.Toolbox
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

    private lateinit var category: Category
    private lateinit var mainViewModel: MainViewModel
    private lateinit var scrollToEndScroller: RecyclerView.SmoothScroller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        category = Toolbox.getDefaultGson().fromJson(json, Category::class.java)
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

    private fun addCategoryBreadCrumb(category: Category) {
        context?.let {
            val item = LayoutInflater.from(it)
                    .inflate(R.layout.item_breadcrumb, view_breadcrumb_container, false) as TextView
            item.id = category.id.toInt()
            item.text = category.name
            item.setOnClickListener {
                if (this.category != category) {
                    this.category = category
                    setupBreadCrumb()

                    viewModel.loadChildCategory(category)
                    firstLoad()
                }
            }

            view_breadcrumb_container.addView(item, 0)

            category.parent?.let {
                addCategoryBreadCrumb(it)
            }
        }
    }

    private fun setupBreadCrumb() {
        view_breadcrumb_container.removeAllViews()
        addCategoryBreadCrumb(category)
        view_breadcrumb_container.post { view_breadcrumb.fullScroll(View.FOCUS_RIGHT) }
    }

    private fun openProductDetail(product: ProductProvider) {
        context?.let {
            if (product is IdentityData) {
                val intent = Intent(it, ProductDetailActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_ID, product.id)
                startActivity(intent)
            }
        }
    }

    private fun setupProducts(view: View) {
        view_recyclerview.adapter = productAdapter
        productAdapter.listener = object: ClickableAdapter.BaseAdapterAction<ProductProvider> {
            override fun click(position: Int, data: ProductProvider, code: Int) {
                openProductDetail(data)
            }

        }
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
                if (data is Category) {
                    category = data
                    setupBreadCrumb()

                    viewModel.loadChildCategory(category)
                    firstLoad()
                }
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

        mainViewModel = obtainViewModel(MainViewModel::class.java, true)

        viewModel = obtainViewModel(ProductsByCategoryViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.childCategories.observe(this, Observer { c ->
            c?.let {
                view_child_categories.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                childAdapter.replaceAll(it)
            }
        })
        viewModel.products.observe(this, Observer { p ->
            p?.let {
                if (reloadData) {
                    productAdapter.replaceAll(it)
                    view_recyclerview.scheduleLayoutAnimation()
                } else
                    productAdapter.addAll(it)

                finishLoading()
            }
        })

        viewModel.loadChildCategory(category)

        firstLoad()
    }

    private fun firstLoad() {
        reloadData = true
        swipe.isRefreshing = true
        productAdapter.clear()
        scrollListener.resetState()

        val request = CategoriedProductsRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = 0
        request.categoryId = category.id
        viewModel.loadProductsByCategory(request)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false
        swipe.isRefreshing = true

        val request = CategoriedProductsRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = currentCount
        request.categoryId = category.id
        viewModel.loadProductsByCategory(request)
    }

    private fun finishLoading() {
        swipe.isRefreshing = false
    }

}