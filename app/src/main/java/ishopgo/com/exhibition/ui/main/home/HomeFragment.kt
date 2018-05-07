package ishopgo.com.exhibition.ui.main.home

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.banner.BannerImageFragment
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandAdapter
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider
import ishopgo.com.exhibition.ui.main.brand.popular.PopularBrandsActivity
import ishopgo.com.exhibition.ui.main.home.category.CategoryAdapter
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.branded.ProductsOfBrandActivity
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.main.product.favorite.FavoriteProductsActivity
import ishopgo.com.exhibition.ui.main.product.popular.PopularProductsActivity
import ishopgo.com.exhibition.ui.main.product.viewed.ViewedProductsActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class HomeFragment : BaseFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel

    private val highlightProductAdapter = ProductAdapter(0.4f)
    private val suggestedProductAdapter = ProductAdapter()
    private val viewedProductAdapter = ProductAdapter(0.4f)
    private val favoriteProductAdapter = ProductAdapter(0.4f)
    private val highlightBrandAdapter = HighlightBrandAdapter(0.4f)
    private val categoriesAdapter = CategoryAdapter()
    private var mPagerAdapter: FragmentPagerAdapter? = null
    private var changePage = Runnable {
        val currentItem = view_banner_pager.currentItem
        val nextItem = (currentItem + 1) % (mPagerAdapter?.count ?: 1)
        view_banner_pager.setCurrentItem(nextItem, nextItem != 0)

        doChangeBanner()
    }

    private fun doChangeBanner() {
        if (mPagerAdapter?.count ?: 1 > 1) {
            view_banner_pager.handler?.let {
                it.removeCallbacks(changePage)
                it.postDelayed(changePage, 2500)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = obtainViewModel(MainViewModel::class.java, true)
        mainViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })

        viewModel = obtainViewModel(HomeViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.suggestedProducts.observe(this, Observer { p ->
            p?.let {
                suggestedProductAdapter.replaceAll(it)
            }
        })
        viewModel.favoriteProducts.observe(this, Observer { p ->
            p?.let {
                favoriteProductAdapter.replaceAll(it)
                view_list_favorite_products.scheduleLayoutAnimation()
            }
        })
        viewModel.viewedProducts.observe(this, Observer { p ->
            p?.let {
                viewedProductAdapter.replaceAll(it)
                view_list_viewed_products.scheduleLayoutAnimation()
            }
        })
        viewModel.highlightProducts.observe(this, Observer { p ->
            p?.let {
                highlightProductAdapter.replaceAll(it)
                view_list_highlight_products.scheduleLayoutAnimation()
            }
        })
        viewModel.highlightBrand.observe(this, Observer { b ->
            b?.let {
                highlightBrandAdapter.replaceAll(it)
                view_list_highlight_brand.scheduleLayoutAnimation()
            }
        })
        viewModel.categories.observe(this, Observer { c ->
            c?.let {
                categoriesAdapter.replaceAll(it)
                rv_categories.scheduleLayoutAnimation()

                swipe.isRefreshing = false
            }
        })
        viewModel.banners.observe(this, Observer { b ->
            b?.let {
                if (it.isEmpty()) {
                    container_banner.visibility = View.GONE
                } else {
                    container_banner.visibility = View.VISIBLE
                    showBanners(it)
                }
            }
        })

        loadData()
    }

    override fun onResume() {
        super.onResume()

        doChangeBanner()

    }

    override fun onStop() {
        super.onStop()

        view_banner_pager.handler?.removeCallbacks(changePage)
    }

    private fun showBanners(imageUrls: List<String>) {
        mPagerAdapter = object : FragmentPagerAdapter(childFragmentManager) {

            override fun getItem(position: Int): Fragment {
                val params = Bundle()
                params.putString(Const.TransferKey.EXTRA_URL, imageUrls[position])
                return BannerImageFragment.newInstance(params)
            }

            override fun getCount(): Int {
                return imageUrls.size
            }
        }
        view_banner_pager.offscreenPageLimit = imageUrls.size
        view_banner_pager.adapter = mPagerAdapter
        view_banner_indicator.setViewPager(view_banner_pager)

        view_banner_pager.post {
            if (imageUrls.size > 1)
                doChangeBanner()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupReferencedProducts(view.context)
        setupSponsorsProducts(view.context)
        setupHighlightProducts(view.context)
        setupCategories(view.context)
        setupViewedProducts(view.context)
        setupFavoriteProducts(view.context)

        setupListeners()
    }

    private fun setupListeners() {
        more_highlight_brand.setOnClickListener {
            openPopularBrands()
        }

        more_highlight_products.setOnClickListener {
            openHighlightProducts()
        }

        swipe.setOnRefreshListener {
            loadData()
        }

        categoriesAdapter.listener = object : ClickableAdapter.BaseAdapterAction<CategoryProvider> {

            override fun click(position: Int, data: CategoryProvider, code: Int) {
                when (code) {
                    CategoryAdapter.TYPE_CHILD -> {
                        drawer_layout.closeDrawer(Gravity.START)
                        mainViewModel.showCategoriedProducts(data)
                    }
                    CategoryAdapter.TYPE_PARENT -> {
                        drawer_layout.closeDrawer(Gravity.START)
                        mainViewModel.showCategoriedProducts(data)
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

    private fun openHighlightProducts() {
        context?.let {
            val intent = Intent(it, PopularProductsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openPopularBrands() {
        context?.let {
            val intent = Intent(it, PopularBrandsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun openViewedProducts() {
        context?.let {
            val intent = Intent(it, ViewedProductsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun openFavoriteProducts() {
        context?.let {
            val intent = Intent(it, FavoriteProductsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadData() {
        viewModel.loadHighlightBrands()
        viewModel.loadHighlightProducts()
        viewModel.loadFavoriteProducts()
        viewModel.loadSuggestedProducts()
        viewModel.loadViewedProducts()
        viewModel.loadCategories()
        viewModel.loadBanners()

        if (UserDataManager.currentUserId > 0) {
            val dummy = LoadMoreRequest()
            dummy.limit = 20
            dummy.offset = 0
            viewModel.loadNotifications(dummy)
        }
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
                val intent = Intent(it, ProductsOfBrandActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_ID, brand.id)
                startActivity(intent)
            }
        }
    }

    private fun setupReferencedProducts(context: Context) {
        view_list_suggest_products.adapter = suggestedProductAdapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_list_suggest_products.layoutManager = layoutManager
        view_list_suggest_products.isNestedScrollingEnabled = false
        view_list_suggest_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        view_list_suggest_products.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom)
    }

    private fun setupSponsorsProducts(context: Context) {
        view_list_highlight_brand.adapter = highlightBrandAdapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        view_list_highlight_brand.layoutManager = layoutManager
        view_list_highlight_brand.isNestedScrollingEnabled = false
        view_list_highlight_brand.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        view_list_highlight_brand.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom)
    }

    private fun setupViewedProducts(context: Context) {
        view_list_viewed_products.adapter = viewedProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_list_viewed_products.layoutManager = layoutManager
        view_list_viewed_products.isNestedScrollingEnabled = false
        view_list_viewed_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        view_list_viewed_products.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.linear_layout_animation_from_bottom)
    }

    private fun setupFavoriteProducts(context: Context) {
        view_list_favorite_products.adapter = favoriteProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_list_favorite_products.layoutManager = layoutManager
        view_list_favorite_products.isNestedScrollingEnabled = false
        view_list_favorite_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        view_list_favorite_products.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.linear_layout_animation_from_bottom)
    }

    private fun setupHighlightProducts(context: Context) {
        view_list_highlight_products.adapter = highlightProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_list_highlight_products.layoutManager = layoutManager
        view_list_highlight_products.isNestedScrollingEnabled = false
        view_list_highlight_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        view_list_highlight_products.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.linear_layout_animation_from_bottom)
    }

    private fun setupCategories(context: Context) {
        rv_categories.adapter = categoriesAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_categories.layoutManager = layoutManager
        rv_categories.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        rv_categories.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.linear_layout_animation_from_bottom)
    }
}