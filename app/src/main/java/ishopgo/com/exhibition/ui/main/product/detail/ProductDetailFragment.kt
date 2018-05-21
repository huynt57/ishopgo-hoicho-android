package ishopgo.com.exhibition.ui.main.product.detail

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
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
import ishopgo.com.exhibition.ui.widget.Toolbox
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.fragment_product_detail.*
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter

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
    private var postMedias: ArrayList<PostMedia> = ArrayList()
    private var adapterImages = ComposingPostMediaAdapter()
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
                container_products_same_shop.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                sameShopProductsAdapter.replaceAll(it)
            }
        })
        viewModel.favoriteProducts.observe(this, Observer { p ->
            p?.let {
                container_favorite.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                favoriteProductAdapter.replaceAll(it)
            }
        })
        viewModel.viewedProducts.observe(this, Observer { p ->
            p?.let {
                container_viewed.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                viewedProductAdapter.replaceAll(it)
            }
        })
        viewModel.productComments.observe(this, Observer { c ->
            c?.let {
                view_list_comments.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                view_divider_111.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                view_product_show_more_comment.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                productCommentAdapter.replaceAll(it)
            }

        })

        viewModel.postCommentSuccess.observe(this, Observer {
            toast("Tạo thảo luận thành công")
            hideProgressDialog()
            edt_comment.setText("")
            postMedias.clear()
            adapterImages.replaceAll(postMedias)
            rv_comment_community_image.visibility = View.GONE
            viewModel.loadProductComments(productId)
        })

        viewModel.getProductLike.observe(this, Observer { c ->
            c?.let {
                Glide.with(context)
                        .load(if (it.status == 1) R.drawable.ic_added_to_favorite_24dp else R.drawable.ic_add_to_favorite_24dp)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(view_favorite)
            }
        })

        viewModel.postLikeSuccess.observe(this, Observer {
            if (UserDataManager.currentUserId > 0)
                viewModel.getProductLike(productId)
        })

        loadData(productId)
    }

    private fun showProductDetail(product: ProductDetailProvider) {
        context?.let {
            Glide.with(it)
                    .load(product.provideProductImage())
                    .apply(RequestOptions()
                            .centerCrop()
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

            Glide.with(context)
                    .load(if (product.provideLiked()) R.drawable.ic_added_to_favorite_24dp else R.drawable.ic_add_to_favorite_24dp)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder))
                    .into(view_favorite)
            view_product_price.text = product.provideProductPrice()

            container_product_brand.visibility = if (product.provideProductBrand().isBlank()) View.GONE else View.VISIBLE
            view_product_brand.text = product.provideProductBrand()

            view_product_description.setHtml(product.provideProductShortDescription(), HtmlHttpImageGetter(view_product_description))
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
            more_products_same_shop.setOnClickListener { openProductsOfShop(it.context, product) }
            more_favorite.setOnClickListener { openFavoriteProducts(it.context) }
            more_viewed.setOnClickListener { openViewedProducts(it.context) }
            container_product_brand.setOnClickListener {
                if (product is ProductDetail) {
                    val brandId = product.department?.id ?: -1L
                    if (brandId != -1L)
                        showProductsOfBrand(brandId)
                }
            }


            img_comment_gallery.setOnClickListener { launchPickPhotoIntent() }

            img_comment_sent.setOnClickListener {
                if (checkRequireFields(edt_comment.text.toString())) {
                    showProgressDialog()
                    viewModel.postCommentProduct(productId, edt_comment.text.toString(), 0, postMedias)
                }
            }
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    private fun checkRequireFields(content: String): Boolean {
        if (content.trim().isEmpty()) {
            toast("Nội dung quá ngắn hoặc chưa đầy đủ")
            return false
        }
        return true
    }

    private fun showDialogLogin() {
        context?.let {
            val builder = MaterialDialog.Builder(it)
            builder.title("Thông báo")
                    .content("Bạn cần đăng nhập để sử dụng tính năng này!")
                    .positiveText("Đăng nhập")
                    .positiveColor(Color.parseColor("#00c853"))
                    .onPositive { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(context, LoginSelectOptionActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
                        startActivity(intent)
                        activity?.finish()
                    }
                    .negativeText("Bỏ qua")
                    .negativeColor(Color.parseColor("#00c853"))
                    .show()

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
        intent.putExtra(Const.TransferKey.EXTRA_JSON, product.provideProductShortDescription())
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
        setupImageRecycleview()
        setupListeners()

    }

    private fun setupImageRecycleview() {
        rv_comment_community_image.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_comment_community_image.adapter = adapterImages
        adapterImages.listener = object : ClickableAdapter.BaseAdapterAction<PostMedia> {
            override fun click(position: Int, data: PostMedia, code: Int) {
                postMedias.remove(data)
                if (postMedias.isEmpty()) rv_comment_community_image.visibility = View.GONE
                adapterImages.replaceAll(postMedias)
            }
        }

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
    }

    private fun showProductsOfBrand(brandId: Long) {
        val intent = Intent(context, ProductsOfBrandActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, brandId)
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

    private fun openProductsOfShop(context: Context, product: ProductDetailProvider) {
        if (product is ProductDetail) {
            val boothId = product.booth?.id
            val intent = Intent(context, ProductsOfShopActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_ID, boothId)
            startActivity(intent)
        }
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
        viewModel.loadProductDetail(productId)
        viewModel.loadProductComments(productId)

        val isUserLoggedIn = UserDataManager.currentUserId > 0
        if (isUserLoggedIn) {
            viewModel.loadViewedProducts(productId)
            viewModel.loadFavoriteProducts(productId)
        }

        container_viewed.visibility = if (isUserLoggedIn) View.VISIBLE else View.GONE
        container_favorite.visibility = if (isUserLoggedIn) View.VISIBLE else View.GONE
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (data.clipData == null) {
                if (Toolbox.exceedSize(context, data.data, (5 * 1024 * 1024).toLong())) {
                    toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                    return
                }
                val postMedia = PostMedia()
                postMedia.uri = data.data
                postMedias.add(postMedia)


            } else {
                for (i in 0 until data.clipData.itemCount) {
                    if (Toolbox.exceedSize(context, data.clipData.getItemAt(i).uri, (2 * 1024 * 1024).toLong())) {
                        toast("Chỉ đính kèm được ảnh có dung lượng dưới 2 MB. Hãy chọn file khác.")
                        return
                    }
                    val postMedia = PostMedia()
                    postMedia.uri = data.clipData.getItemAt(i).uri
                    postMedias.add(postMedia)
                }
            }
            adapterImages.replaceAll(postMedias)
            rv_comment_community_image.visibility = View.VISIBLE
        }
    }
}