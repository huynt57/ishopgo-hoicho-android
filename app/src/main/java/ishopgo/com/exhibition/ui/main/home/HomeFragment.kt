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
import ishopgo.com.exhibition.domain.response.Banner
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.banner.BannerImageFragment
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandAdapter
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider
import ishopgo.com.exhibition.ui.main.brand.popular.PopularBrandsActivity
import ishopgo.com.exhibition.ui.main.home.category.CategoryAdapter
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
import ishopgo.com.exhibition.ui.main.home.category.CategoryStage1Adapter
import ishopgo.com.exhibition.ui.main.home.post.post.PostActivity
import ishopgo.com.exhibition.ui.main.home.post.question.QuestionActivity
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.branded.ProductsOfBrandActivity
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.main.product.favorite.FavoriteProductsActivity
import ishopgo.com.exhibition.ui.main.product.popular.PopularProductsActivity
import ishopgo.com.exhibition.ui.main.product.suggested.SuggestedProductsActivity
import ishopgo.com.exhibition.ui.main.product.viewed.ViewedProductsActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class HomeFragment : BaseFragment() {

    companion object {
        private const val CHANGE_BANNER_PERIOD = 3000L
    }
    private lateinit var viewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel

    private val highlightProductAdapter = ProductAdapter(0.4f)
    private val suggestedProductAdapter = ProductAdapter()
    private val viewedProductAdapter = ProductAdapter(0.4f)
    private val favoriteProductAdapter = ProductAdapter(0.4f)
    private val categoryStage1Adapter = CategoryStage1Adapter(0.3f)
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
                it.postDelayed(changePage, CHANGE_BANNER_PERIOD)
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
                container_suggest_products.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                suggestedProductAdapter.replaceAll(it)
                view_list_suggest_products.scheduleLayoutAnimation()
            }
        })
        viewModel.favoriteProducts.observe(this, Observer { p ->
            p?.let {
                container_favorite_products.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                favoriteProductAdapter.replaceAll(it)
                view_list_favorite_products.scheduleLayoutAnimation()
            }
        })
        viewModel.viewedProducts.observe(this, Observer { p ->
            p?.let {
                container_viewed_products.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                viewedProductAdapter.replaceAll(it)
                view_list_viewed_products.scheduleLayoutAnimation()
            }
        })
        viewModel.highlightProducts.observe(this, Observer { p ->
            p?.let {
                container_highlight_products.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                highlightProductAdapter.replaceAll(it)
                view_list_highlight_products.scheduleLayoutAnimation()
            }
        })
        viewModel.highlightBrand.observe(this, Observer { b ->
            b?.let {
                container_highlight_brand.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                highlightBrandAdapter.replaceAll(it)
                view_list_highlight_brand.scheduleLayoutAnimation()
            }
        })
        viewModel.categories.observe(this, Observer { c ->
            c?.let {
                categoriesAdapter.replaceAll(it)
                rv_categories.scheduleLayoutAnimation()

                categoryStage1Adapter.replaceAll(it)
                view_list_category_stage1.scheduleLayoutAnimation()

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

    private fun showBanners(bannerImages: List<Banner>) {
        mPagerAdapter = object : FragmentPagerAdapter(childFragmentManager) {

            override fun getItem(position: Int): Fragment {
                val params = Bundle()
                params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(bannerImages[position]))
                return BannerImageFragment.newInstance(params)
            }

            override fun getCount(): Int {
                return bannerImages.size
            }
        }
        view_banner_pager.offscreenPageLimit = bannerImages.size
        view_banner_pager.adapter = mPagerAdapter
        view_banner_indicator.setViewPager(view_banner_pager)

        view_banner_pager.post {
            if (bannerImages.size > 1)
                doChangeBanner()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSuggestedProducts(view.context)
        setupHighlightBrands(view.context)
        setupHighlightProducts(view.context)
        setupCategories(view.context)
        setupCategoryStage1(view.context)
        setupViewedProducts(view.context)
        setupFavoriteProducts(view.context)

        setupListeners()
    }

    private fun openPostManager(typeManager: Int) {
        context?.let {
            val intent = Intent(it, PostActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, typeManager)
            startActivity(intent)
        }
    }

    private fun setupListeners() {
        more_highlight_brand.setOnClickListener {
            openPopularBrands()
        }
        more_highlight_products.setOnClickListener {
            openHighlightProducts()
        }
        more_viewed_products.setOnClickListener {
            openViewedProducts()
        }
        more_favorite_products.setOnClickListener {
            openFavoriteProducts()
        }
        more_suggest_products.setOnClickListener {
            openSuggestedProducts()
        }
        swipe.setOnRefreshListener {
            loadData()
        }

        view_open_news.setOnClickListener {
            openPostManager(Const.AccountAction.ACTION_NEWS_MANAGER)
        }
        view_open_commons.setOnClickListener {
            openPostManager(Const.AccountAction.ACTION_GENEREL_MANAGER)
        }
        view_open_quesions.setOnClickListener {
            openQuestionAnswer()
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
        categoryStage1Adapter.listener = object : ClickableAdapter.BaseAdapterAction<CategoryProvider> {
            override fun click(position: Int, data: CategoryProvider, code: Int) {
                mainViewModel.showCategoriedProducts(data)
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

    private fun openQuestionAnswer() {
        context?.let {
            val intent = Intent(it, QuestionActivity::class.java)
            startActivity(intent)
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

    private fun openSuggestedProducts() {
        context?.let {
            val intent = Intent(it, SuggestedProductsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadData() {
        viewModel.loadHighlightBrands()
        viewModel.loadHighlightProducts()
        viewModel.loadSuggestedProducts()
        viewModel.loadCategories()
        viewModel.loadBanners()

        val isUserLoggedIn = UserDataManager.currentUserId > 0
        if (isUserLoggedIn) {
            viewModel.loadViewedProducts()
            viewModel.loadFavoriteProducts()
        }

        container_viewed_products.visibility = if (isUserLoggedIn) View.VISIBLE else View.GONE
        container_favorite_products.visibility = if (isUserLoggedIn) View.VISIBLE else View.GONE
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
                intent.putExtra(Const.TransferKey.EXTRA_TITLE, brand.provideName())
                startActivity(intent)
            }
        }
    }

    private fun setupSuggestedProducts(context: Context) {
        view_list_suggest_products.adapter = suggestedProductAdapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_list_suggest_products.layoutManager = layoutManager
        view_list_suggest_products.isNestedScrollingEnabled = false
        view_list_suggest_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        view_list_suggest_products.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom)
    }

    private fun setupHighlightBrands(context: Context) {
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
        view_list_viewed_products.layoutManager = layoutManager
        view_list_viewed_products.isNestedScrollingEnabled = false
        view_list_viewed_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        view_list_viewed_products.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.linear_layout_animation_from_bottom)
    }

    private fun setupFavoriteProducts(context: Context) {
        view_list_favorite_products.adapter = favoriteProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_favorite_products.layoutManager = layoutManager
        view_list_favorite_products.isNestedScrollingEnabled = false
        view_list_favorite_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        view_list_favorite_products.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.linear_layout_animation_from_bottom)
    }

    private fun setupCategoryStage1(context: Context) {
        view_list_category_stage1.adapter = categoryStage1Adapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        view_list_category_stage1.layoutManager = layoutManager
        view_list_category_stage1.isNestedScrollingEnabled = false
        view_list_category_stage1.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        view_list_category_stage1.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom)
    }

    private fun setupHighlightProducts(context: Context) {
        view_list_highlight_products.adapter = highlightProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_highlight_products.layoutManager = layoutManager
        view_list_highlight_products.isNestedScrollingEnabled = false
        view_list_highlight_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        view_list_highlight_products.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.linear_layout_animation_from_bottom)
    }

    private fun setupCategories(context: Context) {
        rv_categories.adapter = categoriesAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_categories.layoutManager = layoutManager
        rv_categories.isNestedScrollingEnabled = false
//        rv_categories.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        rv_categories.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.linear_layout_animation_from_bottom)
    }
}