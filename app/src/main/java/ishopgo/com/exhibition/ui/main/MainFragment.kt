package ishopgo.com.exhibition.ui.main

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import com.google.gson.Gson
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandAdapter
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider
import ishopgo.com.exhibition.ui.main.category.CategoryAdapter
import ishopgo.com.exhibition.ui.main.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.brand.ProductsByBrandActivity
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MainFragment : BaseActionBarFragment() {

    private lateinit var viewModel: MainViewModel

    private val highlightProductAdapter = ProductAdapter()
    private val suggestedProductAdapter = ProductAdapter()
    private val viewedProductAdapter = ProductAdapter()
    private val favoriteProductAdapter = ProductAdapter()
    private val highlightBrandAdapter = HighlightBrandAdapter()
    private val categoriesAdapter = CategoryAdapter()

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_main
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(MainViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.suggestedProducts.observe(this, Observer { p ->
            p?.let {
                suggestedProductAdapter.replaceAll(it)
            }
        })
        viewModel.favoriteProducts.observe(this, Observer { p ->
            p?.let {
                favoriteProductAdapter.replaceAll(it)
            }
        })
        viewModel.viewedProducts.observe(this, Observer { p ->
            p?.let {
                viewedProductAdapter.replaceAll(it)
            }
        })
        viewModel.highlightProducts.observe(this, Observer { p ->
            p?.let {
                highlightProductAdapter.replaceAll(it)
            }
        })
        viewModel.highlightBrand.observe(this, Observer { b ->
            b?.let {
                highlightBrandAdapter.replaceAll(it)
            }
        })
        viewModel.categories.observe(this, Observer { c ->
            c?.let {
                categoriesAdapter.replaceAll(it)

                swipe.isRefreshing = false
            }
        })

        loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        setupReferencedProducts(view.context)
        setupSponsorsProducts(view.context)
        setupHighlightProducts(view.context)
        setupCategories(view.context)

        setupListeners()

        startupParams()
    }

    private fun startupParams() {
        label_favorite.isSelected = true
        label_viewed.isSelected = false
        label_suggest.isSelected = false
    }

    private fun setupListeners() {
        label_suggest.setOnClickListener {
            label_suggest.isSelected = true
            label_viewed.isSelected = false
            label_favorite.isSelected = false
            view_list_referenced_products.adapter = suggestedProductAdapter
        }

        label_viewed.setOnClickListener {
            label_suggest.isSelected = false
            label_viewed.isSelected = true
            label_favorite.isSelected = false
            view_list_referenced_products.adapter = viewedProductAdapter
        }

        label_favorite.setOnClickListener {
            label_suggest.isSelected = false
            label_viewed.isSelected = false
            label_favorite.isSelected = true
            view_list_referenced_products.adapter = favoriteProductAdapter
        }

        more_sponsors.setOnClickListener {
            // show all brands
        }

        more_highlight_products.setOnClickListener {
            // show all highlight products
        }

        swipe.setOnRefreshListener {
            loadData()
        }

        categoriesAdapter.listener = object : ClickableAdapter.BaseAdapterAction<CategoryProvider> {

            override fun click(position: Int, data: CategoryProvider, code: Int) {
                when (code) {
                    CategoryAdapter.TYPE_CHILD -> {

                    }
                    CategoryAdapter.TYPE_PARENT -> {

                    }
                    else -> {

                    }
                }
            }

        }
        highlightBrandAdapter.listener = object : ClickableAdapter.BaseAdapterAction<HighlightBrandProvider> {
            override fun click(position: Int, data: HighlightBrandProvider, code: Int) {
                openProductsByBrand(data)
            }

        }
        favoriteProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProvider> {
            override fun click(position: Int, data: ProductProvider, code: Int) {
                openProductDetail(data)
            }

        }
        suggestedProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProvider> {
            override fun click(position: Int, data: ProductProvider, code: Int) {
                openProductDetail(data)
            }

        }
        viewedProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProvider> {
            override fun click(position: Int, data: ProductProvider, code: Int) {
                openProductDetail(data)
            }

        }
        highlightProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProvider> {
            override fun click(position: Int, data: ProductProvider, code: Int) {
                openProductDetail(data)
            }

        }

    }

    private fun loadData() {
        viewModel.loadHighlightBrands()
        viewModel.loadHighlightProducts()
        viewModel.loadFavoriteProducts()
        viewModel.loadSuggestedProducts()
        viewModel.loadViewedProducts()
        viewModel.loadCategories()
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Tìm kiếm")
        val titleView = toolbar.getTitleView()
        titleView.setBackgroundResource(R.drawable.bg_search_box)
        titleView.setTextColor(resources.getColor(R.color.colorGrey_700))
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        toolbar.leftButton(R.drawable.ic_drawer_toggle_24dp)
        toolbar.setLeftButtonClickListener {
            if (!drawer_layout.isDrawerVisible(Gravity.START))
                drawer_layout.openDrawer(Gravity.START);
            else drawer_layout.closeDrawer(Gravity.START);
        }
        toolbar.rightButton(R.drawable.ic_search_24dp)
        toolbar.setRightButtonClickListener {
            toast("Search screen")
        }
    }

    private fun setupReferencedProducts(context: Context) {
        view_list_referenced_products.adapter = favoriteProductAdapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_list_referenced_products.layoutManager = layoutManager
        view_list_referenced_products.isNestedScrollingEnabled = false
    }

    private fun setupSponsorsProducts(context: Context) {
        view_list_sponsors.adapter = highlightBrandAdapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        view_list_sponsors.layoutManager = layoutManager
        view_list_sponsors.isNestedScrollingEnabled = false
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

    private fun openProductsByBrand(brand: HighlightBrandProvider) {
        context?.let {
            if (brand is IdentityData) {
                val intent = Intent(it, ProductsByBrandActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_ID, brand.id)
                startActivity(intent)
            }
        }
    }

    private fun setupHighlightProducts(context: Context) {
        view_list_highlight_products.adapter = highlightProductAdapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_list_highlight_products.layoutManager = layoutManager
        view_list_highlight_products.isNestedScrollingEnabled = false
    }

    private fun setupCategories(context: Context) {
        rv_categories.adapter = categoriesAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_categories.layoutManager = layoutManager
    }
}