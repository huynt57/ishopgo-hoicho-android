package ishopgo.com.exhibition.ui.main.home

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.view.animation.AnimationUtils
import ishopgo.com.exhibition.BuildConfig
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.ExposRequest
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.post.PostObject
import ishopgo.com.exhibition.ui.banner.BannerImageFragment
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandAdapter
import ishopgo.com.exhibition.ui.main.brand.popular.PopularBrandsActivity
import ishopgo.com.exhibition.ui.main.home.category.CategoryAdapter
import ishopgo.com.exhibition.ui.main.home.category.CategoryStage1Adapter
import ishopgo.com.exhibition.ui.main.home.introduction.IntroductionActivity
import ishopgo.com.exhibition.ui.main.home.post.LatestPostsAdapter
import ishopgo.com.exhibition.ui.main.home.post.post.PostActivity
import ishopgo.com.exhibition.ui.main.home.post.post.detail.PostMenuDetailActivity
import ishopgo.com.exhibition.ui.main.home.post.question.QuestionActivity
import ishopgo.com.exhibition.ui.main.map.ExpoDetailActivity
import ishopgo.com.exhibition.ui.main.map.config.ExpoMapConfigActivity
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.branded.ProductsOfBrandActivity
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import ishopgo.com.exhibition.ui.main.product.favorite.FavoriteProductsActivity
import ishopgo.com.exhibition.ui.main.product.popular.PopularProductsActivity
import ishopgo.com.exhibition.ui.main.product.suggested.SuggestedProductsActivity
import ishopgo.com.exhibition.ui.main.product.viewed.ViewedProductsActivity
import ishopgo.com.exhibition.ui.main.product.newest.NewestProductsActivity
import ishopgo.com.exhibition.ui.main.product.promotion.PromotionProductsActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_home.*
import android.annotation.SuppressLint
import ishopgo.com.exhibition.ui.main.home.expo_fair.ExpoFairHomeFragment
import ishopgo.com.exhibition.ui.main.registerbooth.RegisterBoothActivity


/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class HomeFragment : BaseFragment() {

    companion object {
        private const val CHANGE_BANNER_PERIOD = 3000L
        const val TYPE_GOING = 1
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewFragment: View
    private val handleOverwrite: HomeFragmentOverwrite = CustomHomeFragment()

    private val highlightProductAdapter = ProductAdapter(0.4f)
    private val viewedProductAdapter = ProductAdapter(0.4f)
    private val favoriteProductAdapter = ProductAdapter(0.4f)
    private val newestProductAdapter = ProductAdapter(0.4f)
    private val promotionProductAdapter = ProductAdapter(0.4f)
    private val expoConfigAdapter = HomeExpoConfigAdapter()
    private val categoryStage1Adapter = CategoryStage1Adapter(0.3f)
    private val highlightBrandAdapter = HighlightBrandAdapter(0.4f)
    private val latestNewsAdapter = LatestPostsAdapter(0.6f)
    private val categoriesAdapter = CategoryAdapter()
    private var mPagerAdapter: FragmentPagerAdapter? = null
    private var mPagerExpoFairAdapter: FragmentPagerAdapter? = null
    private var animationSettingUp = false
    private var changePage = Runnable {
        if (view_banner_pager != null) {
            val currentItem = view_banner_pager.currentItem
            val nextItem = (currentItem + 1) % (mPagerAdapter?.count ?: 1)
            view_banner_pager.setCurrentItem(nextItem, nextItem != 0)

            doChangeBanner()
        }

        if (view_expo_fair_pager != null) {
            val currentItem = view_expo_fair_pager.currentItem
            val nextItem = (currentItem + 1) % (mPagerExpoFairAdapter?.count ?: 1)
            view_expo_fair_pager.setCurrentItem(nextItem, nextItem != 0)

            doChangeExpoFair()
        }
    }

    private fun doChangeBanner() {
        if (mPagerAdapter?.count ?: 1 > 1) {
            view_banner_pager.handler?.let {
                it.removeCallbacks(changePage)
                it.postDelayed(changePage, CHANGE_BANNER_PERIOD)
            }
        }
    }

    private fun doChangeExpoFair() {
        if (mPagerExpoFairAdapter?.count ?: 1 > 1) {
            view_expo_fair_pager.handler?.let {
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
        viewModel.favoriteProducts.observe(this, Observer { p ->
            p?.let {
                container_favorite_products.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                favoriteProductAdapter.replaceAll(it)
                view_list_favorite_products.scheduleLayoutAnimation()
            }
        })
        viewModel.newestProducts.observe(this, Observer { p ->
            p?.let {
                container_newest_products.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                newestProductAdapter.replaceAll(it)
                view_list_newest_products.scheduleLayoutAnimation()
            }
        })
        viewModel.promotionProducts.observe(this, Observer { p ->
            p?.let {
                container_promotion_products.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                promotionProductAdapter.replaceAll(it)
                view_list_promotion_products.scheduleLayoutAnimation()
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
        viewModel.latestNews.observe(this, Observer { b ->
            b?.let {
                container_latest_news.visibility = if (it.posts?.isEmpty() == true) View.GONE else View.VISIBLE
                latestNewsAdapter.replaceAll(it.posts ?: listOf())
                view_list_latest_news.scheduleLayoutAnimation()
            }
        })
        viewModel.categories.observe(this, Observer { c ->
            c?.let {
                categoriesAdapter.replaceAll(it)

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

        viewModel.exposFair.observe(this, Observer { p ->
            p?.let {
                if (it.isNotEmpty()) {
                    setupExpoFair(it as MutableList<ExpoConfig>)
//                    expoConfigAdapter.replaceAll(listExpoConfig)
                    container_expo.visibility = View.VISIBLE
                } else {
                    container_expo.visibility = View.GONE
                }
            }
        })

        viewModel.footer.observe(viewLifeCycleOwner!!, Observer { p ->
            p?.let {
                view_company_info.text = it.asHtml()
            }
        })

        loadData()
        viewModel.loadFooter()
    }

    override fun onResume() {
        super.onResume()
        if (view_banner_pager != null) {
            doChangeBanner()
        }
        if (view_expo_fair_pager != null)
            doChangeExpoFair()
    }

    override fun onStop() {
        super.onStop()

        if (view_banner_pager != null) {
            view_banner_pager.handler?.removeCallbacks(changePage)
        }

        if (view_expo_fair_pager != null) {
            view_expo_fair_pager.handler?.removeCallbacks(changePage)
        }
    }

    private fun showBanners(bannerImages: List<Banner>) {
        if (view_banner_pager != null) {
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
                doChangeBanner()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewFragment = view
        val context = view.context

        setupHighlightBrands(context)
        setupHighlightProducts(context)
        setupCategories(context)
        setupCategoryStage1(context)
        setupViewedProducts(context)
        setupLatestNews(context)
        setupFavoriteProducts(context)
        setupNewestProducts(context)
        setupPromotionProducts(context)
        setupListeners()

        if ((UserDataManager.currentUserId != 0L && UserDataManager.currentType.isEmpty()) || UserDataManager.currentType == "Thành viên")
            if (nestedScroll != null && constrain_moGianHang != null)
                nestedScroll.viewTreeObserver.addOnScrollChangedListener {
                    if (nestedScroll.scrollY <= 300) {
                        constrain_moGianHang.visibility = View.VISIBLE
                    } else {
                        constrain_moGianHang.visibility = View.GONE
                    }
                }
            else constrain_moGianHang.visibility = View.GONE

        btn_moGianHang.setOnClickListener {
            val intent = Intent(context, RegisterBoothActivity::class.java)
            startActivity(intent)
        }

        view_info_version.text = "Phiên bản ${BuildConfig.VERSION_NAME}"
    }

    // To animate view slide out from bottom to top

    private fun openIntroduction() {
        val intent = Intent(requireContext(), IntroductionActivity::class.java)
        startActivity(intent)
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
        more_promotion_products.setOnClickListener {
            openPromotionProducts()
        }
        more_newest_products.setOnClickListener {
            openNewestProducts()
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
        more_latest_news.setOnClickListener {
            openPostManager(Const.AccountAction.ACTION_NEWS_MANAGER)
        }
        swipe.setOnRefreshListener {
            if (!animationSettingUp) {
                view_list_highlight_brand.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom)
                view_list_latest_news.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.linear_layout_animation_from_bottom)
                view_list_viewed_products.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.linear_layout_animation_from_bottom)
                view_list_favorite_products.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.linear_layout_animation_from_bottom)
                view_list_category_stage1.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom)
                view_list_highlight_products.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom)
                animationSettingUp = true
            }
            loadData()
        }

        view_open_introduce.setOnClickListener {
            openIntroduction()
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
        view_open_expo_map.setOnClickListener {
            openExpoMap()
        }
        more_expo.setOnClickListener { openExpoMap() }

        categoriesAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Category> {

            override fun click(position: Int, data: Category, code: Int) {
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
        latestNewsAdapter.listener = object : ClickableAdapter.BaseAdapterAction<PostObject> {
            override fun click(position: Int, data: PostObject, code: Int) {
                val i = Intent(context, PostMenuDetailActivity::class.java)
                i.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                startActivity(i)
            }

        }
        highlightBrandAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Brand> {
            override fun click(position: Int, data: Brand, code: Int) {
                if (data.id != -1L) // prevent click dummy data of first loading
                    openProductsByBrand(data)
            }

        }
        favoriteProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                if (data.id != -1L) // prevent click dummy data of first loading
                    openProductDetail(data)
            }

        }
        newestProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                if (data.id != -1L) // prevent click dummy data of first loading
                    openProductDetail(data)
            }

        }
        promotionProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                if (data.id != -1L) // prevent click dummy data of first loading
                    openProductDetail(data)
            }

        }
        categoryStage1Adapter.listener = object : ClickableAdapter.BaseAdapterAction<Category> {
            override fun click(position: Int, data: Category, code: Int) {
                if (data.id != -1L) // prevent click dummy data of first loading
                    mainViewModel.showCategoriedProducts(data)
            }

        }
        viewedProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                if (data.id != -1L) // prevent click dummy data of first loading
                    openProductDetail(data)
            }

        }
        highlightProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                if (data.id != -1L) // prevent click dummy data of first loading
                    openProductDetail(data)
            }

        }

    }

    private fun openNewestProducts() {
        context?.let {
            val intent = Intent(it, NewestProductsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openPromotionProducts() {
        context?.let {
            val intent = Intent(it, PromotionProductsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openQuestionAnswer() {
        context?.let {
            val intent = Intent(it, QuestionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openExpoMap() {
        context?.let {
            val intent = Intent(it, ExpoMapConfigActivity::class.java)
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
        firtsLoadExpoFair(TYPE_GOING)
        viewModel.loadBanners()
        viewModel.loadCategories()
        viewModel.loadHighlightBrands()
        viewModel.loadHighlightProducts()
        viewModel.loadNewestProducts()
        viewModel.loadPromotionProducts()
        viewModel.loadLatestNews()
        handleOverwrite.handleInOtherFlavor(viewFragment, viewModel, this)

        val isUserLoggedIn = UserDataManager.currentUserId > 0
        if (isUserLoggedIn) {
            viewModel.loadViewedProducts()
            viewModel.loadFavoriteProducts()
        }

        container_viewed_products.visibility = if (isUserLoggedIn) View.VISIBLE else View.GONE
        container_favorite_products.visibility = if (isUserLoggedIn) View.VISIBLE else View.GONE

    }

    private fun firtsLoadExpoFair(currentType: Int) {
        val request = ExposRequest()
        request.limit = Const.PAGE_LIMIT
        request.offset = 0
        request.time = currentType
        viewModel.loadExpoFair(request)
    }

    private fun openProductDetail(product: Product) {
        context?.let {
            val intent = Intent(it, ProductDetailActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_ID, product.id)
            startActivity(intent)
        }
    }

    private fun openProductsByBrand(brand: Brand) {
        context?.let {
            val intent = Intent(it, ProductsOfBrandActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(brand))
            startActivity(intent)
        }
    }

    private fun setupHighlightBrands(context: Context) {
        // dummy product
        val dummy = mutableListOf<Brand>()
        for (i in 0..6) {
            val element = Brand()
            element.id = -1L
            dummy.add(element)
        }
        highlightBrandAdapter.addAll(dummy)

        view_list_highlight_brand.adapter = highlightBrandAdapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        view_list_highlight_brand.layoutManager = layoutManager
        view_list_highlight_brand.isNestedScrollingEnabled = false
        view_list_highlight_brand.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupLatestNews(context: Context) {
        view_list_latest_news.adapter = latestNewsAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_latest_news.layoutManager = layoutManager
        view_list_latest_news.isNestedScrollingEnabled = false
        view_list_latest_news.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupViewedProducts(context: Context) {
        // dummy product
        val dummy = mutableListOf<Product>()
        for (i in 0..6) {
            val element = Product()
            element.id = -1L
            dummy.add(element)
        }
        viewedProductAdapter.addAll(dummy)

        view_list_viewed_products.adapter = viewedProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_viewed_products.layoutManager = layoutManager
        view_list_viewed_products.isNestedScrollingEnabled = false
        view_list_viewed_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupNewestProducts(context: Context) {
        // dummy product
        val dummy = mutableListOf<Product>()
        for (i in 0..6) {
            val element = Product()
            element.id = -1L
            dummy.add(element)
        }
        newestProductAdapter.addAll(dummy)

        view_list_newest_products.adapter = newestProductAdapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        view_list_newest_products.layoutManager = layoutManager
        view_list_newest_products.isNestedScrollingEnabled = false
        view_list_newest_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupExpoFair(listExpo: MutableList<ExpoConfig>) {
        // dummy product
//        val dummy = mutableListOf<ExpoConfig>()
//        val element = ExpoConfig()
//        element.id = -1L
//        dummy.add(element)
        if (view_expo_fair_pager != null) {
            mPagerExpoFairAdapter = object : FragmentPagerAdapter(childFragmentManager) {

                override fun getItem(position: Int): Fragment {
                    val params = Bundle()
                    params.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(listExpo[position]))
                    return ExpoFairHomeFragment.newInstance(params)
                }

                override fun getCount(): Int {
                    return listExpo.size
                }
            }
            view_expo_fair_pager.offscreenPageLimit = listExpo.size
            view_expo_fair_pager.adapter = mPagerExpoFairAdapter
            view_expo_fair_indicator.setViewPager(view_expo_fair_pager)

            view_expo_fair_pager.post {
                doChangeExpoFair()
            }
        }

//        expoConfigAdapter.addAll(dummy)

//        expoConfigAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ExpoConfig> {
//            override fun click(position: Int, data: ExpoConfig, code: Int) {
//                if (data.id != -1L) {
//                    val intent = Intent(requireContext(), ExpoDetailActivity::class.java)
//                    intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
//                    startActivity(intent)
//                }
//            }
//        }

//        view_list_expo_fair.adapter = expoConfigAdapter
//        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        view_list_expo_fair.layoutManager = layoutManager
//        view_list_expo_fair.isNestedScrollingEnabled = false
//        view_list_expo_fair.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupPromotionProducts(context: Context) {
        // dummy product
        val dummy = mutableListOf<Product>()
        for (i in 0..6) {
            val element = Product()
            element.id = -1L
            dummy.add(element)
        }
        promotionProductAdapter.addAll(dummy)

        view_list_promotion_products.adapter = promotionProductAdapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        view_list_promotion_products.layoutManager = layoutManager
        view_list_promotion_products.isNestedScrollingEnabled = false
        view_list_promotion_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupFavoriteProducts(context: Context) {
        // dummy product
        val dummy = mutableListOf<Product>()
        for (i in 0..6) {
            val element = Product()
            element.id = -1L
            dummy.add(element)
        }
        favoriteProductAdapter.addAll(dummy)

        view_list_favorite_products.adapter = favoriteProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_favorite_products.layoutManager = layoutManager
        view_list_favorite_products.isNestedScrollingEnabled = false
        view_list_favorite_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupCategoryStage1(context: Context) {
        // dummy category
        val dummy = mutableListOf<Category>()
        for (i in 0..6) {
            val d = Category()
            d.id = -1L
            dummy.add(d)
        }
        categoryStage1Adapter.addAll(dummy)

        view_list_category_stage1.adapter = categoryStage1Adapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        view_list_category_stage1.layoutManager = layoutManager
        view_list_category_stage1.isNestedScrollingEnabled = false
        view_list_category_stage1.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupHighlightProducts(context: Context) {
        // dummy product
        val dummy = mutableListOf<Product>()
        for (i in 0..6) {
            val element = Product()
            element.id = -1L
            dummy.add(element)
        }
        highlightProductAdapter.addAll(dummy)

        view_list_highlight_products.adapter = highlightProductAdapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        view_list_highlight_products.layoutManager = layoutManager
        view_list_highlight_products.isNestedScrollingEnabled = false
        view_list_highlight_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupCategories(context: Context) {
        rv_categories.adapter = categoriesAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.isAutoMeasureEnabled = true
        rv_categories.layoutManager = layoutManager
        rv_categories.isNestedScrollingEnabled = false
    }
}

