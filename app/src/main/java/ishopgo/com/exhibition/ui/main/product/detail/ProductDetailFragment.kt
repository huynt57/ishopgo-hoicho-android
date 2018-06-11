package ishopgo.com.exhibition.ui.main.product.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.CreateConversationRequest
import ishopgo.com.exhibition.domain.request.ProductSalePointRequest
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.domain.response.ProductComment
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.model.ProductSalePoint
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import ishopgo.com.exhibition.ui.chat.local.profile.ProfileActivity
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.login.LoginSelectOptionActivity
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.branded.ProductsOfBrandActivity
import ishopgo.com.exhibition.ui.main.product.detail.add_sale_point.ProductSalePointAddActivity
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentAdapter
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentProvider
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentsActivity
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.FullDetailActivity
import ishopgo.com.exhibition.ui.main.product.detail.sale_point.ProductSalePointActivity
import ishopgo.com.exhibition.ui.main.product.favorite.FavoriteProductsActivity
import ishopgo.com.exhibition.ui.main.product.shop.ProductsOfShopActivity
import ishopgo.com.exhibition.ui.main.product.viewed.ViewedProductsActivity
import ishopgo.com.exhibition.ui.main.salepointdetail.SalePointDetailActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
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
    private var adapterSalePoint = ProductSalePointAdapter()
    private var productId: Long = -1L
    private var mPagerAdapter: FragmentPagerAdapter? = null
    private var changePage = Runnable {
        val currentItem = view_product_image.currentItem
        val nextItem = (currentItem + 1) % (mPagerAdapter?.count ?: 1)
        view_product_image.setCurrentItem(nextItem, nextItem != 0)

        doChangeBanner()
    }

    private fun doChangeBanner() {
        if (mPagerAdapter?.count ?: 1 > 1) {
            view_product_image.handler?.let {
                it.removeCallbacks(changePage)
                it.postDelayed(changePage, 2500)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productId = arguments?.getLong(Const.TransferKey.EXTRA_ID) ?: -1L
        if (productId == -1L)
            throw RuntimeException("Sai dinh dang")
    }

    private fun firstLoadSalePoint() {
        val firstLoad = ProductSalePointRequest()
        firstLoad.limit = 3
        firstLoad.offset = 0
        firstLoad.productId = productId
        viewModel.getProductSalePoint(firstLoad)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(ProductDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        viewModel.conversation.observe(this, Observer { c ->
            c?.let {
                val conv = LocalConversationItem()
                conv.idConversions = c.id ?: ""
                conv.name = c.name ?: ""

                context?.let {
                    val intent = Intent(it, ConversationActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_CONVERSATION_ID, conv.idConversions)
                    intent.putExtra(Const.TransferKey.EXTRA_TITLE, conv.name)
                    startActivity(intent)
                }
            }
        })
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
                        .load(if (it.status == 1) R.drawable.ic_heart_checked else R.drawable.ic_heart)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(view_shop_follow)
                if (it.status == 1) tv_product_like.text = "Bỏ quan tâm" else tv_product_like.text = "Quan tâm"

                activity?.setResult(RESULT_OK)
            }
        })

        viewModel.postLikeSuccess.observe(this, Observer {
            if (UserDataManager.currentUserId > 0)
                viewModel.getProductLike(productId)
        })

        viewModel.productSalePoint.observe(this, Observer { p ->
            p.let {
                it?.let { it1 -> adapterSalePoint.replaceAll(it1) }
            }
        })

        loadData(productId)
        firstLoadSalePoint()
    }

    @SuppressLint("SetTextI18n")
    private fun showProductDetail(product: ProductDetailProvider) {
        context?.let {

            if (product.provideViewWholesale()) {
                view_product_wholesale.visibility = View.VISIBLE
                view_product_wholesale.text = product.provideWholesale()
            } else view_product_wholesale.visibility = View.GONE

            if (product is ProductDetail) {
                if (product.images != null && product.images!!.isNotEmpty())
                    showBanners(product.images!!)
            }

            view_product_name.text = product.provideProductName()
            if (UserDataManager.currentUserId > 0) {
                linear_shop_follow.setOnClickListener { viewModel.postProductLike(productId) }
                view_share.setOnClickListener { showDialogShare(product) }
            } else {
                view_shop_follow.setOnClickListener {
                    openActivtyLogin()
                }
                view_share.setOnClickListener {
                    openActivtyLogin()
                }
            }

            Glide.with(context)
                    .load(if (product.provideLiked()) R.drawable.ic_heart_checked else R.drawable.ic_heart)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder))
                    .into(view_shop_follow)
            view_product_price.text = product.provideProductPrice()

            if (product.provideLiked()) tv_product_like.text = "Bỏ quan tâm" else tv_product_like.text = "Quan tâm"

            container_product_brand.visibility = if (product.provideProductBrand().isBlank()) View.GONE else View.VISIBLE
            view_product_brand.text = product.provideProductBrand()

            view_product_description.setHtml(product.provideProductShortDescription(), HtmlHttpImageGetter(view_product_description))
            view_shop_name.text = product.provideShopName()
            view_shop_region.text = "Khu vực: ${product.provideShopRegion()}"
            view_shop_product_count.text = "<b>${product.provideShopProductCount()}</b><br>Sản phẩm".asHtml()
            view_shop_rating.text = "<b>${product.provideShopRateCount()}</b><br>Đánh giá".asHtml()
            view_product_like_count.text = "${product.provideProductLikeCount()} thích"
            view_product_comment_count.text = "${product.provideProductCommentCount()} bình luận"
            view_product_share_count.text = "${product.provideProductShareCount()} chia sẻ"
            tv_shop_phone.text = product.provideShopPhone()
            tv_shop_address.text = product.provideShopAddress()
            view_shop_detail.setOnClickListener { openShopDetail(it.context, product) }
            view_shop_call.setOnClickListener { callShop(it.context, product) }
            view_shop_message.setOnClickListener { messageShop(it.context, product) }
            view_product_description.setOnClickListener { showProductFullDescription(it.context, product) }
            view_product_show_more_description.setOnClickListener { showProductFullDescription(it.context, product) }
            view_product_show_more_comment.setOnClickListener { showMoreComment(it.context, product) }
            view_product_show_more_sale_point.setOnClickListener { showMoreSalePoint(it.context, product) }
            more_products_same_shop.setOnClickListener { openProductsOfShop(it.context, product) }
            more_favorite.setOnClickListener { openFavoriteProducts(it.context) }
            more_viewed.setOnClickListener { openViewedProducts(it.context) }
            container_product_brand.setOnClickListener {
                if (product is ProductDetail) {
                    val brandId = product.department?.id ?: -1L
                    val brandName = product.department?.name ?: "Sản phẩm cùng thương hiệu"
                    if (brandId != -1L)
                        showProductsOfBrand(brandId, brandName)
                }
            }

            if (UserDataManager.currentUserId > 0) {
                img_comment_gallery.setOnClickListener { launchPickPhotoIntent() }
                img_comment_sent.setOnClickListener {
                    if (checkRequireFields(edt_comment.text.toString())) {
                        showProgressDialog()
                        viewModel.postCommentProduct(productId, edt_comment.text.toString(), 0, postMedias)
                    }
                }
                view_shop_add_sale_point.setOnClickListener { openAddSalePoint(it.context, product) }
                edt_comment.isFocusable = true
                edt_comment.isFocusableInTouchMode = true
                edt_comment.setOnClickListener(null)
            } else {
                img_comment_gallery.setOnClickListener { openActivtyLogin() }
                img_comment_sent.setOnClickListener { openActivtyLogin() }
                view_shop_add_sale_point.setOnClickListener { openActivtyLogin() }
                edt_comment.isFocusable = false
                edt_comment.isFocusableInTouchMode = false
                edt_comment.setOnClickListener { openActivtyLogin() }
            }
        }
        openProductSalePoint(product)
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

    private fun openActivtyLogin() {
        val intent = Intent(context, LoginSelectOptionActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
        startActivity(intent)
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

    private fun showMoreSalePoint(context: Context, product: ProductDetailProvider) {
        if (product is IdentityData) {
            val intent = Intent(context, ProductSalePointActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))
            startActivityForResult(intent, Const.RequestCode.PRODUCT_SALE_POINT_DETAIL)
        }
    }

    private fun messageShop(context: Context, product: ProductDetailProvider) {
        if (UserDataManager.currentUserId > 0) {
            // gui tin nhan cho shop
            if (product is ProductDetail) {
                val boothId = product.booth?.id
                boothId?.let {
                    val request = CreateConversationRequest()
                    request.type = 1
                    val members = mutableListOf<Long>()
                    members.add(UserDataManager.currentUserId)
                    members.add(it)
                    request.member = members
                    viewModel.createConversation(request)
                }
            }
        } else {
            openActivtyLogin()
        }
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
        setupSalePointRecycleview()
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

    private fun setupSalePointRecycleview() {
        rv_product_sale_point.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        ViewCompat.setNestedScrollingEnabled(rv_product_sale_point, false)
        rv_product_sale_point.adapter = adapterSalePoint
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

    private fun showProductsOfBrand(brandId: Long, brandName: String) {
        val intent = Intent(context, ProductsOfBrandActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, brandId)
        intent.putExtra(Const.TransferKey.EXTRA_TITLE, brandName)
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

    private fun openAddSalePoint(context: Context, product: ProductDetailProvider) {
        val intent = Intent(context, ProductSalePointAddActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))
        startActivityForResult(intent, Const.RequestCode.SALE_POINT_ADD)
    }

    private fun openProductSalePoint(product: ProductDetailProvider) {
        adapterSalePoint.listener = object : ClickableAdapter.BaseAdapterAction<ProductSalePointProvider> {
            override fun click(position: Int, data: ProductSalePointProvider, code: Int) {
                if (data is ProductSalePoint) {
                    val intent = Intent(context, SalePointDetailActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, data.phone)
                    intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))
                    startActivity(intent)
                }
            }
        }
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

        productCommentAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductCommentProvider> {
            override fun click(position: Int, data: ProductCommentProvider, code: Int) {
                if (data is ProductComment) {
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                    startActivity(intent)
                }
            }
        }
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

        if (requestCode == Const.RequestCode.PRODUCT_SALE_POINT_DETAIL && resultCode == Activity.RESULT_OK)
            firstLoadSalePoint()

        if (requestCode == Const.RequestCode.SALE_POINT_ADD && resultCode == Activity.RESULT_OK)
            firstLoadSalePoint()

        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (data.clipData == null) {
                if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                    toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                    return
                }
                val postMedia = PostMedia()
                postMedia.uri = data.data
                postMedias.add(postMedia)


            } else {
                for (i in 0 until data.clipData.itemCount) {
                    if (Toolbox.exceedSize(context!!, data.clipData.getItemAt(i).uri, (5 * 1024 * 1024).toLong())) {
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

    override fun onStop() {
        super.onStop()

        view_product_image.handler?.removeCallbacks(changePage)
    }

    private fun showBanners(imagesList: MutableList<String>) {
        mPagerAdapter = object : FragmentPagerAdapter(childFragmentManager) {

            override fun getItem(position: Int): Fragment {
                val params = Bundle()
                params.putString(Const.TransferKey.EXTRA_STRING_LIST, imagesList[position])
                return ImagesProductFragment.newInstance(params)
            }

            override fun getCount(): Int {
                return imagesList.size
            }
        }
        view_product_image.offscreenPageLimit = imagesList.size
        view_product_image.adapter = mPagerAdapter
        view_banner_indicator.setViewPager(view_product_image)

        view_product_image.post {
            if (imagesList.size > 1)
                doChangeBanner()
        }
    }
}