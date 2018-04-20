package ishopgo.com.exhibition.ui.main.product.detail

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.brand.ProductsByBrandActivity
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.FullDetailFragment
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_product_detail.*

class ProductDetailFragment : BaseActionBarFragment() {

    companion object {
        fun newInstance(params: Bundle): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private lateinit var viewModel: ProductDetailViewModel

    private val sameShopProductsAdapter = ProductAdapter(0.4f)
    private val viewedProductAdapter = ProductAdapter(0.4f)
    private val favoriteProductAdapter = ProductAdapter(0.4f)
    private var productId: Long = -1L

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_product_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productId = arguments?.getLong(Const.TransferKey.EXTRA_ID) ?: -1L
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ProductDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.detail.observe(this, Observer { d ->
            d?.let {
                showProductDetail(it)
            }
        })
        viewModel.sameShopProducts.observe(this, Observer { p ->
            p?.let {
                sameShopProductsAdapter.replaceAll(it)
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

        loadData(productId)
    }

    private fun showProductDetail(product: ProductDetailProvider) {
        context?.let {
            Glide.with(it)
                    .load(product.provideProductImage())
                    .apply(RequestOptions().placeholder(R.drawable.image_placeholder))
                    .into(view_product_image)

            view_product_name.text = product.provideProductName()
            view_favorite
            view_share

            view_product_price.text = product.provideProductPrice()
            view_product_brand.text = product.provideProductBrand()
            view_product_description.text = product.provideProductShortDescription()
            view_product_show_more_description.setOnClickListener {
                val intent = Intent(it.context, FullDetailFragment::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_JSON, Gson().toJson(product))
                startActivity(intent)
            }
            view_shop_name.text = product.provideShopName()
            view_shop_region.text = product.provideShopRegion()
            view_shop_detail.setOnClickListener {
                if (product is IdentityData) {
                    // find brand id of this product
                    val intent = Intent(it.context, ShopDetailActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_ID, Gson().toJson(product))
                    startActivity(intent)
                }
            }
            view_shop_product_count.text = "<b>${product.provideShopProductCount()}</b>\nSản phẩm mới".asHtml()
            view_shop_rating.text = "<b>${product.provideShopRateCount()}</b>\nĐánh giá".asHtml()
            view_shop_call.setOnClickListener {
                // make call to shop
            }
            view_shop_message.setOnClickListener {
                // send message to shop or switch to ichat
            }
            view_product_like_count.text = "${product.provideProductLikeCount()} thích"
            view_product_comment_count.text = "${product.provideProductCommentCount()} bình luận"
            view_product_share_count.text = "${product.provideProductShareCount()} chia sẻ"
            view_list_comments
            view_product_show_more_comment.setOnClickListener {
                // show all comment of this product
            }
            more_products_same_shop.setOnClickListener {
                // show all products of this shop
            }
            view_list_products_same_shop
            more_favorite.setOnClickListener {
                // show all my favorite products
            }
            view_list_favorite
            more_viewed.setOnClickListener {
                // show all viewed products
            }
            view_list_viewed
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        setupFavoriteProducts(view.context)
        setupSameShopProducts(view.context)
        setupViewedProducts(view.context)

        setupListeners()

    }

    private fun setupListeners() {
        more_viewed.setOnClickListener {
            // show all viewed products
        }

        more_favorite.setOnClickListener {
            // show all favorited products
        }

        more_products_same_shop.setOnClickListener {
            // show all same shop products
        }

        favoriteProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProvider> {
            override fun click(position: Int, data: ProductProvider, code: Int) {
                openProductDetail(data)
            }

        }
        sameShopProductsAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProvider> {
            override fun click(position: Int, data: ProductProvider, code: Int) {
                openProductDetail(data)
            }

        }
        viewedProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProvider> {
            override fun click(position: Int, data: ProductProvider, code: Int) {
                openProductDetail(data)
            }

        }

    }

    private fun loadData(productId: Long) {
        viewModel.loadSameShopProducts(productId)
        viewModel.loadFavoriteProducts(productId)
        viewModel.loadViewedProducts(productId)
        viewModel.loadProductDetail(productId)
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Tìm kiếm")
        val titleView = toolbar.getTitleView()
        titleView.setBackgroundResource(R.drawable.bg_search_box)
        titleView.setTextColor(resources.getColor(R.color.colorGrey_700))
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        toolbar.leftButton(R.drawable.ic_drawer_toggle_24dp)
        toolbar.setLeftButtonClickListener {

        }
    }

    private fun setupFavoriteProducts(context: Context) {
        view_list_favorite.adapter = favoriteProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_favorite.layoutManager = layoutManager
        view_list_favorite.isNestedScrollingEnabled = false
    }

    private fun setupSameShopProducts(context: Context) {
        view_list_products_same_shop.adapter = sameShopProductsAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_products_same_shop.layoutManager = layoutManager
        view_list_products_same_shop.isNestedScrollingEnabled = false
    }

    private fun openProductDetail(product: ProductProvider) {
        context?.let {
            val intent = Intent(it, ProductDetailActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, Gson().toJson(product))
            startActivity(intent)
        }
    }

    private fun openProductsByBrand(brand: HighlightBrandProvider) {
        context?.let {
            val intent = Intent(it, ProductsByBrandActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, Gson().toJson(brand))
            startActivity(intent)
        }
    }

    private fun setupViewedProducts(context: Context) {
        view_list_viewed.adapter = viewedProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_viewed.layoutManager = layoutManager
        view_list_viewed.isNestedScrollingEnabled = false
    }

}
