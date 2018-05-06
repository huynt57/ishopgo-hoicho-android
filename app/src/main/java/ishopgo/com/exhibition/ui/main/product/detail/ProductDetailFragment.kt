package ishopgo.com.exhibition.ui.main.product.detail

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.login.LoginSelectOptionActivity
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.branded.ProductsOfBrandActivity
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentAdapter
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentsActivity
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.FullDetailActivity
import ishopgo.com.exhibition.ui.main.product.favorite.FavoriteProductsActivity
import ishopgo.com.exhibition.ui.main.product.shop.ProductsOfShopActivity
import ishopgo.com.exhibition.ui.main.product.viewed.ViewedProductsActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.fragment_product_detail.*

/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class ProductDetailFragment : BaseFragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
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
        viewModel.productComments.observe(this, Observer { c ->
            c?.let {
                productCommentAdapter.replaceAll(it)
            }

        })

        viewModel.getProductLike.observe(this, Observer { c ->
            c?.let {
                if (it.status == 0)
                    Glide.with(context)
                            .load(R.drawable.ic_added_to_favorite_24dp)
                            .apply(RequestOptions()
                                    .placeholder(R.drawable.image_placeholder)
                                    .error(R.drawable.image_placeholder))
                            .into(view_favorite)
                else Glide.with(context)
                        .load(R.drawable.ic_add_to_favorite_24dp)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(view_favorite)
            }
        })

        viewModel.postLikeSuccess.observe(this, Observer {
            viewModel.getProductLike(productId)
        })

        loadData(productId)
    }

    private fun showProductDetail(product: ProductDetailProvider) {
        context?.let {
            Glide.with(it)
                    .load(product.provideProductImage())
                    .apply(RequestOptions()
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder))
                    .into(view_product_image)

            view_product_name.text = product.provideProductName()
            if (UserDataManager.currentUserId > 0) {
                view_favorite.setOnClickListener { viewModel.postProductLike(productId) }
                view_share.setOnClickListener { showDialogShare(product) }
            } else {
                view_favorite.setOnClickListener { showDialogLogin() }
                view_share.setOnClickListener { showDialogLogin() }
            }


            view_product_price.text = product.provideProductPrice()
            view_product_brand.text = product.provideProductBrand()
            view_product_description.text = product.provideProductShortDescription()
            view_shop_name.text = product.provideShopName()
            view_shop_region.text = "Khu vực: ${product.provideShopRegion()}"
            view_shop_product_count.text = "<b>${product.provideShopProductCount()}</b><br>Sản phẩm mới".asHtml()
            view_shop_rating.text = "<b>${product.provideShopRateCount()}</b><br>Đánh giá".asHtml()
            view_product_like_count.text = "${product.provideProductLikeCount()} thích"
            view_product_comment_count.text = "${product.provideProductCommentCount()} bình luận"
            view_product_share_count.text = "${product.provideProductShareCount()} chia sẻ"

            view_shop_detail.setOnClickListener { openShopDetail(it.context, product) }
            view_shop_call.setOnClickListener { callShop(it.context, product) }
            view_shop_message.setOnClickListener { messageShop(it.context, product) }
            view_product_show_more_description.setOnClickListener { showProductFullDescription(it.context, product) }
            view_product_show_more_comment.setOnClickListener { showMoreComment(it.context, product) }
            more_products_same_shop.setOnClickListener { openProductsOfShop(it.context, productId) }
            more_favorite.setOnClickListener { openFavoriteProducts(it.context) }
            more_viewed.setOnClickListener { openViewedProducts(it.context) }
        }
    }

    private fun showDialogLogin() {
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Thông báo")
            builder.setMessage("Bạn cần đăng nhập để sử dụng tính năng này!")
            builder.setPositiveButton("Đăng nhập") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(context, LoginSelectOptionActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }

            builder?.setNegativeButton("Bỏ qua") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog?.show()

            val positiveButton = dialog?.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton?.setTextColor(Color.parseColor("#00c853"))

            val negativeButton = dialog?.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton?.setTextColor(Color.parseColor("#00c853"))
        }
    }

    private fun showDialogShare(product: ProductDetailProvider) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_community_share, false)
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()
            val tv_share_facebook = dialog.findViewById(R.id.tv_share_facebook) as VectorSupportTextView
            tv_share_facebook.setOnClickListener {
                shareFacebook(product)
                viewModel.postShareProduct(productId)
            }
            val tv_share_zalo = dialog.findViewById(R.id.tv_share_zalo) as VectorSupportTextView
            tv_share_zalo.setOnClickListener {
                shareApp(product)
                viewModel.postShareProduct(productId)
            }
            dialog.show()
        }
    }

    private fun shareFacebook(product: ProductDetailProvider) {
        val urlToShare = product.provideProductLinkAffiliate()
        var intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"

        intent.putExtra(Intent.EXTRA_TEXT, urlToShare)

        var facebookAppFound = false
        val matches = context!!.packageManager.queryIntentActivities(intent, 0)
        for (info in matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.`package` = info.activityInfo.packageName
                facebookAppFound = true
                break
            }
        }

        if (!facebookAppFound) {
            val sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=$urlToShare"
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
        }

        startActivity(intent)
    }

    private fun shareApp(product: ProductDetailProvider) {
        val urlToShare = product.provideProductLinkAffiliate()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, urlToShare)
        startActivity(shareIntent)
    }

    private fun showProductFullDescription(context: Context, product: ProductDetailProvider) {
        val intent = Intent(context, FullDetailActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, Gson().toJson(product))
        startActivity(intent)
    }

    private fun showMoreComment(context: Context, product: ProductDetailProvider) {
        if (product is IdentityData) {
            val intent = Intent(context, ProductCommentsActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_ID, product.id)
            startActivityForResult(intent, Const.RequestCode.EDIT_PRODUCT_COMMENT)
        }
    }

    private fun messageShop(context: Context, product: ProductDetailProvider) {
        // gui tin nhan cho shop
    }

    private fun callShop(context: Context, product: ProductDetailProvider) {
        val phoneNumber = product.provideShopPhone()
        val call = Uri.parse("tel:" + phoneNumber)
        val intent = Intent(Intent.ACTION_DIAL, call)
        if (intent.resolveActivity(context.packageManager) != null)
            startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductComments(view.context)
        setupFavoriteProducts(view.context)
        setupSameShopProducts(view.context)
        setupViewedProducts(view.context)

        setupListeners()

    }

    private fun setupListeners() {
        favoriteProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProvider> {
            override fun click(position: Int, data: ProductProvider, code: Int) {
                context?.let { openProductDetail(it, data) }
            }

        }
        sameShopProductsAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProvider> {
            override fun click(position: Int, data: ProductProvider, code: Int) {
                context?.let { openProductDetail(it, data) }
            }

        }
        viewedProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProvider> {
            override fun click(position: Int, data: ProductProvider, code: Int) {
                context?.let { openProductDetail(it, data) }
            }

        }

        container_product_brand.setOnClickListener { showProductsOfBrand(productId) }
    }

    private fun showProductsOfBrand(productId: Long) {
        val intent = Intent(context, ProductsOfBrandActivity::class.java)
        startActivity(intent)
    }

    private fun openFavoriteProducts(context: Context) {
        val intent = Intent(context, FavoriteProductsActivity::class.java)
        startActivity(intent)

    }

    private fun openViewedProducts(context: Context) {
        val intent = Intent(context, ViewedProductsActivity::class.java)
        startActivity(intent)
    }

    private fun openProductDetail(context: Context, product: ProductProvider) {
        if (product is IdentityData) {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_ID, product.id)
            startActivity(intent)
        }
    }

    private fun openProductsOfShop(context: Context, productId: Long) {
        val intent = Intent(context, ProductsOfShopActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, productId)
        startActivity(intent)
    }

    private fun openShopDetail(context: Context, product: ProductDetailProvider) {
        if (product is ProductDetail) {
            val boothId = product.booth?.id
            val intent = Intent(context, ShopDetailActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_ID, boothId)
            startActivity(intent)
        }
    }

    private fun loadData(productId: Long) {
        viewModel.loadSameShopProducts(productId)
        viewModel.loadFavoriteProducts(productId)
        viewModel.loadViewedProducts(productId)
        viewModel.loadProductDetail(productId)
        viewModel.loadProductComments(productId)
        viewModel.getProductLike(productId)
    }

    private fun setupProductComments(context: Context) {
        view_list_comments.adapter = productCommentAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.isAutoMeasureEnabled = true
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
}