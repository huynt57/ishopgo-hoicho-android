package ishopgo.com.exhibition.ui.main.product.detail

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
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
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.brand.ProductsByBrandActivity
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentAdapter
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.FullDetailActivity
import ishopgo.com.exhibition.ui.main.product.favorite.FavoriteProductsActivity
import ishopgo.com.exhibition.ui.main.product.viewed.ViewedProductsActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
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
    private val productCommentAdapter = ProductCommentAdapter()
    private var productId: Long = -1L

    override fun contentLayoutRes(): Int {
        return R.layout.fragment_product_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productId = arguments?.getLong(Const.TransferKey.EXTRA_ID) ?: -1L
        if (productId == -1L)
            throw RuntimeException("Sai dinh dang")
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
                val intent = Intent(it.context, FullDetailActivity::class.java)
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
                openProductsByBrand(productId)
            }
            view_list_products_same_shop
            more_favorite.setOnClickListener {
                openFavoriteProducts()
            }
            view_list_favorite
            more_viewed.setOnClickListener {
                openViewedProducts()
            }
            view_list_viewed
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        setupProductComments(view.context)
        setupFavoriteProducts(view.context)
        setupSameShopProducts(view.context)
        setupViewedProducts(view.context)

        setupListeners()

    }

    private fun setupListeners() {
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
        toolbar.setCustomTitle("Chi tiết sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }

    private fun setupProductComments(context: Context) {
        view_list_comments.adapter = productCommentAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_comments.layoutManager = layoutManager
        view_list_comments.isNestedScrollingEnabled = false
        view_list_comments.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupFavoriteProducts(context: Context) {
        view_list_favorite.adapter = favoriteProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_favorite.layoutManager = layoutManager
        view_list_favorite.isNestedScrollingEnabled = false
        view_list_favorite.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupSameShopProducts(context: Context) {
        view_list_products_same_shop.adapter = sameShopProductsAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_products_same_shop.layoutManager = layoutManager
        view_list_products_same_shop.isNestedScrollingEnabled = false
        view_list_products_same_shop.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupViewedProducts(context: Context) {
        view_list_viewed.adapter = viewedProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_viewed.layoutManager = layoutManager
        view_list_viewed.isNestedScrollingEnabled = false
        view_list_viewed.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun openFavoriteProducts() {
        context?.let {
            val intent = Intent(it, FavoriteProductsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun openViewedProducts() {
        context?.let {
            val intent = Intent(it, ViewedProductsActivity::class.java)
            startActivity(intent)
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

    private fun openProductsByBrand(productId: Long) {
        context?.let {
            val intent = Intent(it, ProductsByBrandActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_ID, productId)
            startActivity(intent)
        }
    }

}
