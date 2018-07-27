package ishopgo.com.exhibition.ui.main.home.category.product

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.CategoriedProductsRequest
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.FilterProduct
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.filterproduct.FilterProductViewModel
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.widget.EndlessRecyclerViewScrollListener
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.content_swipable_recyclerview.*
import kotlinx.android.synthetic.main.empty_list_result.*
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
    private lateinit var filterViewModel: FilterProductViewModel
    private var filterProduct = FilterProduct()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        category = Toolbox.gson.fromJson(json, Category::class.java)
    }

    private val childAdapter = CategoryChildAdapter()
    private val productAdapter = ProductAdapter()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

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
                    .inflate(R.layout.item_breadcrumb, view_breadcrumb_container, false) as VectorSupportTextView
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

    private fun openProductDetail(product: Product) {
        context?.let {
                val intent = Intent(it, ProductDetailActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_ID, product.id)
                startActivity(intent)
        }
    }

    private fun setupProducts(view: View) {
        view_recyclerview.adapter = productAdapter
        productAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
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
        childAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Category> {
            override fun click(position: Int, data: Category, code: Int) {
                    category = data
                    setupBreadCrumb()

                    viewModel.loadChildCategory(category)
                    firstLoad()
                }

        }
    }

    private fun setupToolbar() {
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.onBackPressed()
        }
        toolbar.setCustomTitle("Tìm kiếm sản phẩm")
        val titleView = toolbar.getTitleView()
        titleView.setBackgroundResource(R.drawable.bg_search_box)
        titleView.setTextColor(resources.getColor(R.color.md_grey_700))
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        titleView.setOnClickListener { mainViewModel.searchInCategory(category) }
        titleView.drawableCompat(0, 0, R.drawable.ic_search_highlight_24dp, 0)

        toolbar.rightButton(R.drawable.ic_filter_highlight_24dp)
        toolbar.setRightButtonClickListener {
            filterViewModel.showFragmentFilter(filterProduct)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = obtainViewModel(MainViewModel::class.java, true)

        filterViewModel = obtainViewModel(FilterProductViewModel::class.java, true)
        filterViewModel.getDataFilter.observe(viewLifeCycleOwner!!, Observer { c ->
            c?.let {
                val count = (it.filter?.size
                        ?: 0) + if (it.sort_by?.isNotEmpty() == true && it.sort_type?.isNotEmpty() == true) 1 else 0
                toolbar.rightButton(R.drawable.ic_filter_highlight_24dp, count)
                filterProduct = it
                firstLoad()
            }
        })

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
                    if (it.isEmpty()) {
                        view_empty_result_notice.visibility = View.VISIBLE
                        view_empty_result_notice.text = "Nội dung trống"
                    } else view_empty_result_notice.visibility = View.GONE
                    productAdapter.replaceAll(it)
                    view_recyclerview.scheduleLayoutAnimation()
                } else
                    productAdapter.addAll(it)

                finishLoading()
            }
        })

        viewModel.loadChildCategory(category)
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
        request.sort_by = filterProduct.sort_by
        request.sort_type = filterProduct.sort_type
        request.type_filter = filterProduct.filter ?: mutableListOf()
        viewModel.loadProductsByCategory(request)
    }

    private fun loadMore(currentCount: Int) {
        reloadData = false
        swipe.isRefreshing = true

        val request = CategoriedProductsRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = currentCount
        request.categoryId = category.id
        request.sort_by = filterProduct.sort_by
        request.sort_type = filterProduct.sort_type
        request.type_filter = filterProduct.filter ?: mutableListOf()
        viewModel.loadProductsByCategory(request)
    }

    private fun finishLoading() {
        swipe.isRefreshing = false
    }

}