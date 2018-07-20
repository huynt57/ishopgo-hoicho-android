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
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.request.CreateConversationRequest
import ishopgo.com.exhibition.domain.request.ProductDiaryRequest
import ishopgo.com.exhibition.domain.request.ProductSalePointRequest
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.ProductDetailComment
import ishopgo.com.exhibition.model.ProductSalePoint
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.diary.DiaryProduct
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.extensions.*
import ishopgo.com.exhibition.ui.login.LoginActivity
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.branded.ProductsOfBrandActivity
import ishopgo.com.exhibition.ui.main.product.detail.add_sale_point.ProductSalePointAddActivity
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentAdapter
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentsActivity
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.FullDetailActivity
import ishopgo.com.exhibition.ui.main.product.detail.sale_point.ProductSalePointActivity
import ishopgo.com.exhibition.ui.main.product.favorite.FavoriteProductsActivity
import ishopgo.com.exhibition.ui.main.product.shop.ProductsOfShopActivity
import ishopgo.com.exhibition.ui.main.product.viewed.ViewedProductsActivity
import ishopgo.com.exhibition.ui.main.salepointdetail.SalePointDetailActivity
import ishopgo.com.exhibition.ui.main.shop.ShopDetailActivity
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.content_product_info.*
import kotlinx.android.synthetic.main.fragment_product_detail.*

/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class ProductDetailFragment : BaseFragment(), BackpressConsumable {
    override fun onBackPressConsumed(): Boolean {
        return childFragmentManager.popBackStackImmediate()
    }

    override fun requireInput(): List<String> {
        return listOf(Const.TransferKey.EXTRA_ID)
    }

    companion object {
        fun newInstance(params: Bundle): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            fragment.arguments = params

            return fragment
        }

        const val LIKED = 1
        const val VIEW_WHOLESALE = 1

        const val COMMUNITY_REPLY = 0
        const val COMMUNITY_REPLY_CHILD = 1
        const val COMMUNITY_SHOW_CHILD = 2
        const val COMMUNITY_IMAGE_CLICK = 3

        const val DIARY_IMAGE_CLICK = 0
        const val DIARY_USER_CLICK = 1
    }

    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var ratingViewModel: RatingProductViewModel
    private lateinit var viewModelDiary: DiaryProductViewModel

    private val handleOverwrite: ProductDetailOverwrite = CustomProductDetail()

    private val sameShopProductsAdapter = ProductAdapter(0.4f)
    private val viewedProductAdapter = ProductAdapter(0.4f)
    private val favoriteProductAdapter = ProductAdapter(0.4f)
    private val productCommentAdapter = ProductCommentAdapter()
    private val productProcessAdapter = ProductProcessAdapter()
    private var adapterSalePoint = ProductSalePointAdapter()
    private var adapterDiary = ProductDiaryAdapter()
    private var productId: Long = -1L
    private var mPagerAdapter: FragmentPagerAdapter? = null
    private var changePage = Runnable {
        val currentItem = view_product_image.currentItem
        val nextItem = (currentItem + 1) % (mPagerAdapter?.count ?: 1)
        view_product_image.setCurrentItem(nextItem, nextItem != 0)

        doChangeBanner()
    }

    private fun doChangeBanner() {
        if (mPagerAdapter?.count ?: 1 > 1 && view_product_image != null) {
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

        productId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L
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

    private fun firstLoadDiary() {
        val firstLoad = ProductDiaryRequest()
        firstLoad.limit = 2
        firstLoad.offset = 0
        firstLoad.productId = productId
        viewModel.getProductDiary(firstLoad)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelDiary = obtainViewModel(DiaryProductViewModel::class.java, true)
        viewModelDiary.isSusscess.observe(this, Observer {
            firstLoadDiary()
        })
        ratingViewModel = obtainViewModel(RatingProductViewModel::class.java, true)
        ratingViewModel.errorSignal.observe(this, Observer { error -> error?.let { resolveError(it) } })
        ratingViewModel.isSusscess.observe(this, Observer {
            viewModel.loadProductComments(productId)
        })

        viewModel = obtainViewModel(ProductDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                resolveError(it)
                hideProgressDialog()
            }
        })
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
        viewModel.getProductLike.observe(this, Observer { c ->
            c?.let {
                view_shop_follow.drawableCompat(0, if (it.status == 1) R.drawable.ic_favorite_accent_24dp else R.drawable.ic_favorite_border_default_24dp, 0, 0)
                if (it.status == 1) view_shop_follow.text = "Đã quan tâm" else view_shop_follow.text = "Quan tâm"

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

        viewModel.productDiary.observe(this, Observer { p ->
            p.let {
                it?.let { it1 -> adapterDiary.replaceAll(it1) }
            }
        })

        view?.let {
            handleOverwrite.handleActivityCreated(it, viewModel, this)
        }

        loadData(productId)
        firstLoadSalePoint()
    }

    var productDetail = ProductDetail()

    @SuppressLint("SetTextI18n")
    private fun showProductDetail(product: ProductDetail) {
        context?.let {
            productDetail = product

            view?.let {
                handleOverwrite.handleInOtherFlavor(it, productDetail, this)
            }

            val processes = product.process ?: listOf()
            container_product_process.visibility = if (processes.isEmpty()) View.GONE else View.VISIBLE
            productProcessAdapter.replaceAll(processes)

            val convert = ProductDetailConverter().convert(product)
            if (convert.provideViewWholesale()) {
                view_product_wholesale.visibility = View.VISIBLE
                view_product_wholesale.text = convert.provideWholesale()
                view_product_wholesale_limit.visibility = View.VISIBLE
                view_product_wholesale_limit.text = convert.provideWholesaleLimit()
            } else {
                view_product_wholesale.visibility = View.GONE
                view_product_wholesale_limit.visibility = View.GONE
            }

            if (product.images != null && product.images!!.isNotEmpty())
                showBanners(product.images!!)

            view_product_name.text = convert.provideProductName()
            if (UserDataManager.currentUserId > 0) {
                view_shop_follow.setOnClickListener { viewModel.postProductLike(productId) }
                view_share.setOnClickListener { showDialogShare(product) }
            } else {
                view_shop_follow.setOnClickListener {
                    openActivtyLogin()
                }
                view_share.setOnClickListener {
                    openActivtyLogin()
                }
            }

            view_shop_follow.drawableCompat(0, if (convert.provideLiked()) R.drawable.ic_favorite_accent_24dp else R.drawable.ic_favorite_border_default_24dp, 0, 0)
            view_product_price.text = convert.provideProductPrice()

            if (convert.provideLiked()) view_shop_follow.text = "Đã quan tâm" else view_shop_follow.text = "Quan tâm"

            if (convert.provideMadeIn().isNotEmpty()) {
                view_product_madeIn.visibility = View.VISIBLE
                view_product_madeIn.text = convert.provideMadeIn()
            }

            if (convert.provideNoCode().isNotEmpty()) {
                view_product_no_code.visibility = View.VISIBLE
                view_product_no_code.text = convert.provideNoCode()
            }

            if (convert.provideDateProduce().isNotEmpty()) {
                view_product_date_produce.visibility = View.VISIBLE
                view_product_date_produce.text = convert.provideDateProduce()
            }

            if (convert.provideDateExpected().isNotEmpty()) {
                view_product_date_expected.visibility = View.VISIBLE
                view_product_date_expected.text = convert.provideDateExpected()
            }

            if (convert.provideScale().isNotEmpty()) {
                view_product_scale.visibility = View.VISIBLE
                view_product_scale.text = convert.provideScale()
            }
            if (convert.provideProductCode().isNotEmpty()) {
                view_product_msp.visibility = View.VISIBLE
                view_product_msp.text = convert.provideProductCode()
            }
            if (convert.providePackaging().isNotEmpty()) {
                view_product_packaging.visibility = View.VISIBLE
                view_product_packaging.text = convert.providePackaging()
            }
            if (convert.provideSeason().isNotEmpty()) {
                view_product_season.visibility = View.VISIBLE
                view_product_season.text = convert.provideSeason()
            }

            if (convert.provideNumberExpected().isNotEmpty()) {
                view_product_number_expected.visibility = View.VISIBLE
                view_product_number_expected.text = convert.provideNumberExpected()
            }

            container_product_brand.visibility = if (convert.provideProductBrand().isBlank()) View.GONE else View.VISIBLE
            view_product_brand.text = convert.provideProductBrand()
            view_rating.rating = convert.provideRate()
            tv_rating_result.text = "(${convert.provideRate()}/5.0)"
            val productDesc = convert.provideProductShortDescription().toString()
            if (productDesc.isBlank()) {
                container_description.visibility = View.GONE
                view_product_show_more_description.visibility = View.GONE
            } else {
                container_description.visibility = View.VISIBLE
                view_product_show_more_description.visibility = View.VISIBLE
            }
            view_product_description.loadData(productDesc, "text/html", null)
            view_label_shop_name.text = convert.provideShopLabel()
            view_shop_name.text = convert.provideShopName()
            view_shop_product_count.text = convert.provideShopProductCount()
            view_shop_rating.text = convert.provideShopRatePoint()
            view_product_like_count.text = "${convert.provideProductLikeCount()} thích"
            view_product_comment_count.text = "${convert.provideProductCommentCount()} Đánh giá"
            view_product_share_count.text = "${convert.provideProductShareCount()} chia sẻ"
            tv_shop_phone.text = convert.provideShopPhone()
            tv_shop_phone.setOnClickListener {
                val uri = Uri.parse("tel:${convert.provideShopPhone()}")
                val i = Intent(Intent.ACTION_DIAL, uri)
                it.context.startActivity(i)
            }
            tv_shop_address.text = convert.provideShopAddress()
            view_shop_detail.setOnClickListener { openShopDetail(it.context, product) }
            view_shop_call.setOnClickListener { callShop(it.context, product) }
            view_shop_message.setOnClickListener { messageShop(it.context, product) }
            view_product_show_more_description.setOnClickListener { showProductFullDescription(it.context, product) }
            view_product_show_more_comment.setOnClickListener { showMoreComment(it.context, product) }
            view_product_show_more_sale_point.setOnClickListener { showMoreSalePoint(it.context, product) }
            more_products_same_shop.setOnClickListener { openProductsOfShop(it.context, product) }
            more_favorite.setOnClickListener { openFavoriteProducts(it.context) }
            more_viewed.setOnClickListener { openViewedProducts(it.context) }
            container_product_brand.setOnClickListener {
                val brandId = product.department?.id ?: -1L
                val brandName = product.department?.name ?: "Sản phẩm cùng thương hiệu"
                if (brandId != -1L) {
                    val brand = Brand()
                    brand.id = brandId
                    brand.name = brandName
                    showProductsOfBrand(brand)
                }
            }

            if (UserDataManager.currentUserId > 0) {
                linearLayout.setOnClickListener {
                    val productDetailComment = ProductDetailComment()
                    productDetailComment.product = product
                    productDetailComment.comment = null

                    val extra = Bundle()
                    extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(productDetailComment))
                    Navigation.findNavController(it).navigate(R.id.action_productDetailFragmentActionBar_to_ratingProductFragment, extra)
                }
                edt_comment.setOnClickListener {
                    val productDetailComment = ProductDetailComment()
                    productDetailComment.product = product
                    productDetailComment.comment = null

                    val extra = Bundle()
                    extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(productDetailComment))
                    Navigation.findNavController(it).navigate(R.id.action_productDetailFragmentActionBar_to_ratingProductFragment, extra)
                }

                view_shop_add_sale_point.setOnClickListener { openAddSalePoint(it.context, product) }
            } else {
                linearLayout.setOnClickListener { openActivtyLogin() }
                img_comment_gallery.setOnClickListener { openActivtyLogin() }
                img_comment_sent.setOnClickListener { openActivtyLogin() }
                view_shop_add_sale_point.setOnClickListener { openActivtyLogin() }
                edt_comment.isFocusable = false
                edt_comment.isFocusableInTouchMode = false
                edt_comment.setOnClickListener { openActivtyLogin() }
            }

            if (convert.provideIsDiary()) {
                container_diary.visibility = View.VISIBLE

                if (UserDataManager.currentType == "Quản trị viên") {
                    val listPermission = Const.listPermission
                    if (listPermission.isNotEmpty())
                        for (i in listPermission.indices)
                            if (Const.Permission.EXPO_BOOTH_PRODUCTION_DIARY_ADD == listPermission[i]) {
                                view_add_diary.visibility = View.VISIBLE
                                break
                            }
                } else view_add_diary.visibility = View.GONE


                if (UserDataManager.currentUserId == product.booth?.id)
                    view_add_diary.visibility = View.VISIBLE
                else view_add_diary.visibility = View.GONE

                firstLoadDiary()

            } else container_diary.visibility = View.GONE

        }
        openProductSalePoint(product)

        floatQrCode.setOnClickListener {
            showQrCodeProduct(product)
        }
    }

    private fun showQrCodeProduct(product: ProductDetail) {
        val extra = Bundle()
        extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))

        Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_productDetailFragmentActionBar_to_qrCodeProductFragment, extra)
    }

//    private fun showMoreProductProcess(product: ProductDetail) {
//        val extra = Bundle()
//        extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))
//
//        Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_productDetailFragmentActionBar_to_productProcessFragment, extra)
//    }

    interface ProductDetailProvider {

        fun provideProductImage(): CharSequence
        fun provideProductName(): CharSequence
        fun provideProductPrice(): CharSequence
        fun provideProductBrand(): CharSequence
        fun provideProductShortDescription(): CharSequence
        fun provideShopName(): CharSequence
        fun provideShopLabel(): CharSequence
        fun provideShopRegion(): CharSequence
        fun provideShopProductCount(): CharSequence
        fun provideShopRateCount(): Int
        fun provideShopRatePoint(): CharSequence
        fun provideShopPhone(): CharSequence
        fun provideLiked(): Boolean
        fun provideShopAddress(): CharSequence
        fun provideFollowed(): Boolean

        fun provideProductLikeCount(): Int
        fun provideProductCommentCount(): Int
        fun provideProductShareCount(): Int
        fun provideProductLinkAffiliate(): CharSequence

        fun provideViewWholesale(): Boolean
        fun provideWholesale(): CharSequence
        fun provideWholesaleLimit(): CharSequence

        fun provideProductProcess(): List<CharSequence>
        fun provideRate(): Float

        fun provideProductCode(): CharSequence
        fun providePackaging(): CharSequence
        fun provideMadeIn(): CharSequence
        fun provideNoCode(): CharSequence
        fun provideDateProduce(): CharSequence
        fun provideDateExpected(): CharSequence
        fun provideScale(): CharSequence
        fun provideNumberExpected(): CharSequence
        fun provideSeason(): CharSequence
        fun provideIsDiary(): Boolean
    }

    class ProductDetailConverter : Converter<ProductDetail, ProductDetailProvider> {

        override fun convert(from: ProductDetail): ProductDetailProvider {
            return object : ProductDetailProvider {
                override fun providePackaging(): CharSequence {
                    return if (from.dongGoi.isNullOrBlank()) ""
                    else "<b>Đóng gói: <font color=\"red\">${from.dongGoi}</font></b>".asHtml()
                }

                override fun provideSeason(): CharSequence {
                    return if (from.muaVu.isNullOrBlank()) ""
                    else "<b>Mùa vụ sản xuất: <font color=\"red\">${from.muaVu}</font></b>".asHtml()
                }

                override fun provideProductCode(): CharSequence {
                    return if (from.code.isNullOrBlank()) ""
                    else "<b>Mã sản phẩm: <font color=\"red\">${from.code}</font></b>".asHtml()
                }

                override fun provideIsDiary(): Boolean {
                    return from.isNhatkySx == 1

                }

                override fun provideShopLabel(): CharSequence {
                    return from.booth?.title ?: "Gian hàng trưng bày"
                }

                override fun provideMadeIn(): CharSequence {
                    return if (from.madeIn.isNullOrBlank()) ""
                    else "<b>Xuất xứ: <font color=\"red\">${from.madeIn}</font></b>".asHtml()
                }

                override fun provideNoCode(): CharSequence {
                    return if (from.msLohang.isNullOrBlank()) ""
                    else "<b>Mã số lô: <font color=\"red\">${from.msLohang}</font></b>".asHtml()
                }

                override fun provideDateProduce(): CharSequence {
                    return if (from.ngaySx.isNullOrBlank()) ""
                    else "<b>Ngày bắt đầu sản xuất: <font color=\"red\">${from.ngaySx?.asDate()}</font></b>".asHtml()
                }

                override fun provideDateExpected(): CharSequence {
                    return if (from.dkThuhoach.isNullOrBlank()) ""
                    else "<b>Ngày thu hoạch dự kiến: <font color=\"red\">${from.dkThuhoach?.asDate()}</font></b>".asHtml()
                }

                override fun provideScale(): CharSequence {
                    return if (from.quyMo.isNullOrBlank()) ""
                    else "<b>Quy mô: <font color=\"red\">${from.quyMo}</font></b>".asHtml()
                }

                override fun provideNumberExpected(): CharSequence {
                    return if (from.sanLuong.isNullOrBlank()) ""
                    else "<b>Khả năng cung ứng: <font color=\"red\">${from.sanLuong}</font></b>".asHtml()
                }

                override fun provideShopRatePoint(): CharSequence {
                    return "<b><font color=\"red\">${from.booth?.rate?.toFloat()
                            ?: 0.0f}/5.0</font></b><br>${from.booth?.rateCount
                            ?: 0} Đánh giá".asHtml()
                }

                override fun provideRate(): Float {
                    return from.rate?.toFloat() ?: 0.0f
                }

                override fun provideProductProcess(): List<CharSequence> {
                    val titles = mutableListOf<String>()
                    from.process?.map {
                        if (!it.title.isNullOrEmpty()) titles.add(it.title ?: "")
                    }

                    return titles
                }

                override fun provideWholesaleLimit(): CharSequence {
                    return "Mua tối thiểu <b><font color=\"red\">${from.wholesaleCountProduct}</font></b>".asHtml()
                }

                override fun provideWholesale(): CharSequence {
                    return "<b>Giá bán sỉ: Từ <font color=\"red\">${from.wholesalePriceFrom.asMoney()}</font> tới <font color=\"red\">${from.wholesalePriceTo.asMoney()}</font></b>".asHtml()
                }

                override fun provideViewWholesale(): Boolean {
                    return from.viewWholesale == VIEW_WHOLESALE
                }

                override fun provideShopAddress(): CharSequence {
                    return "${from.booth?.address?.trim() ?: ""}, ${from.booth?.district?.trim()
                            ?: ""}, ${from.booth?.city?.trim() ?: ""}"
                }

                override fun provideLiked(): Boolean {
                    return from.liked == LIKED
                }

                override fun provideProductLinkAffiliate(): CharSequence {
                    return from.linkShare ?: ""
                }

                override fun provideProductImage(): CharSequence {
                    return from.image ?: ""
                }

                override fun provideProductName(): CharSequence {
                    return from.name?.trim() ?: "Tên sản phẩm"
                }

                override fun provideProductPrice(): CharSequence {
                    if (from.price == 0L) {
                        return "<b>Giá bán lẻ: <font color=\"#00c853\">Liên hệ</font></b>".asHtml()
                    } else if (from.promotionPrice != null && from.promotionPrice != from.price) {
                        return "<b>Giá bán lẻ: <font color=\"#BDBDBD\"><strike>${from.price.asMoney()}</strike></font> <font color=\"#00c853\">${from.promotionPrice.asMoney()}</font></b> ".asHtml()
                    } else {
                        return "<b>Giá bán lẻ: <font color=\"#00c853\">${from.price.asMoney()}</font></b>".asHtml()
                    }
                }

                override fun provideProductBrand(): CharSequence {
                    if (from.department?.id == 0L) return ""
                    return from.department?.name?.trim() ?: ""
                }

                override fun provideProductShortDescription(): CharSequence {
                    return if (from.description.isNullOrBlank()) ""
                    else {
                        String.format(
                                "<html><head><meta name=\"viewport\"/><style>%s</style></head><body>%s</body></html>",
                                Const.webViewCSS, from.description)
                    }
                }

                override fun provideShopName(): CharSequence {
                    return from.booth?.name?.trim() ?: ""
                }

                override fun provideShopRegion(): CharSequence {
                    return from.booth?.address?.trim() ?: ""
                }

                override fun provideShopProductCount(): CharSequence {
                    return "<b><font color=\"#00c853\">${from.booth?.count
                            ?: 0}</font></b><br>Sản phẩm".asHtml()
                }

                override fun provideShopRateCount(): Int {
                    return from.booth?.rateCount ?: 0
                }

                override fun provideShopPhone(): CharSequence {
                    return from.booth?.hotline?.asStylePhoneNumber()?.asHtml() ?: ""
                }

                override fun provideProductLikeCount(): Int {
                    return from.likes
                }

                override fun provideProductCommentCount(): Int {
                    return from.comments
                }

                override fun provideProductShareCount(): Int {
                    return from.shares
                }

                override fun provideFollowed(): Boolean {
                    return from.booth?.isFollowed() ?: false
                }
            }
        }
    }

    private fun openActivtyLogin() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
        startActivity(intent)
    }

    private fun showDialogShare(product: ProductDetail) {
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

    private var callbackManager: CallbackManager? = null

    private fun shareFacebook(product: ProductDetail) {
        callbackManager = CallbackManager.Factory.create()
        val shareDialog = ShareDialog(this)

        shareDialog.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
            override fun onSuccess(result: Sharer.Result?) {
            }

            override fun onCancel() {
                toast("Chia sẻ bị huỷ bỏ")
            }

            override fun onError(error: FacebookException?) {
                toast(error.toString())
            }
        })

        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            val urlToShare = product.linkShare.toString()
            val shareContent = ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(urlToShare))
                    .build()

            shareDialog.show(shareContent)
        }
    }

    private fun shareApp(product: ProductDetail) {
        val urlToShare = product.linkShare
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, urlToShare)
        startActivity(shareIntent)
    }

    private fun showProductFullDescription(context: Context, product: ProductDetail) {
        val intent = Intent(context, FullDetailActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, product.description)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        view_scrollview.visibility = View.VISIBLE
        constraintLayout.visibility = View.VISIBLE
    }

    private fun showMoreComment(context: Context, product: ProductDetail) {
        val intent = Intent(context, ProductCommentsActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))
        startActivityForResult(intent, Const.RequestCode.EDIT_PRODUCT_COMMENT)
    }

    private fun showMoreSalePoint(context: Context, product: ProductDetail) {
        val intent = Intent(context, ProductSalePointActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))
        startActivityForResult(intent, Const.RequestCode.PRODUCT_SALE_POINT_DETAIL)
    }

    private fun messageShop(context: Context, product: ProductDetail) {
        if (UserDataManager.currentUserId > 0) {
            // gui tin nhan cho shop
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
        } else {
            openActivtyLogin()
        }
    }

    private fun callShop(context: Context, product: ProductDetail) {
        val phoneNumber = product.booth?.hotline ?: ""
        val call = Uri.parse("tel:" + phoneNumber)
        val intent = Intent(Intent.ACTION_DIAL, call)
        if (intent.resolveActivity(context.packageManager) != null)
            startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductProcess(view.context)
        setupProductComments(view.context)
        setupFavoriteProducts(view.context)
        setupSameShopProducts(view.context)
        setupViewedProducts(view.context)
        setupDiaryProducts(view.context)
        setupSalePointRecycleview()
        setupListeners()

        handleOverwrite.handleViewCreated(view, view.context, this)

        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            if (productId != -1L) loadData(productId)
        }
    }

    private fun setupSalePointRecycleview() {
        rv_product_sale_point.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        ViewCompat.setNestedScrollingEnabled(rv_product_sale_point, false)
        rv_product_sale_point.adapter = adapterSalePoint
    }

    private fun setupListeners() {
        favoriteProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                context?.let { openProductDetail(it, data) }
            }

        }
        sameShopProductsAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                context?.let { openProductDetail(it, data) }
            }

        }
        viewedProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                context?.let { openProductDetail(it, data) }
            }

        }
    }

    private fun showProductsOfBrand(brand: Brand) {
        val intent = Intent(context, ProductsOfBrandActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(brand))
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

    private fun openAddSalePoint(context: Context, product: ProductDetail) {
        val intent = Intent(context, ProductSalePointAddActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))
        startActivityForResult(intent, Const.RequestCode.SALE_POINT_ADD)
    }

    private fun openProductSalePoint(product: ProductDetail) {
        adapterSalePoint.listener = object : ClickableAdapter.BaseAdapterAction<ProductSalePoint> {
            override fun click(position: Int, data: ProductSalePoint, code: Int) {
                val intent = Intent(context, SalePointDetailActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, data.phone)
                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))
                startActivity(intent)
            }
        }
    }

    private fun openProductDetail(context: Context, product: Product) {
        val intent = Intent(context, ProductDetailActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, product.id)
        startActivity(intent)
    }

    private fun openProductsOfShop(context: Context, product: ProductDetail) {
        val boothId = product.booth?.id
        val intent = Intent(context, ProductsOfShopActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, boothId)
        startActivity(intent)
    }

    private fun openShopDetail(context: Context, product: ProductDetail) {
        val boothId = product.booth?.id
        val intent = Intent(context, ShopDetailActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, boothId)
        startActivity(intent)
    }

    private fun loadData(productId: Long) {
        viewModel.loadData(productId)

        val isUserLoggedIn = UserDataManager.currentUserId > 0
        container_viewed.visibility = if (isUserLoggedIn) View.VISIBLE else View.GONE
        container_favorite.visibility = if (isUserLoggedIn) View.VISIBLE else View.GONE
    }

    private fun setupProductProcess(context: Context) {
        productProcessAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProcess> {
            override fun click(position: Int, data: ProductProcess, code: Int) {
                openProcessDetail(data)
            }
        }
        view_product_product_process.adapter = productProcessAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_product_product_process.layoutManager = layoutManager
        view_product_product_process.isNestedScrollingEnabled = false
    }

    private fun openProcessDetail(data: ProductProcess) {
        val intent = Intent(requireContext(), FullDetailActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_TITLE, data.title ?: "")
        intent.putExtra(Const.TransferKey.EXTRA_JSON, data.description ?: "")
        startActivity(intent)
    }

    private fun setupProductComments(context: Context) {
        view_list_comments.adapter = productCommentAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.isAutoMeasureEnabled = true
        view_list_comments.layoutManager = layoutManager
        view_list_comments.isNestedScrollingEnabled = false
        view_list_comments.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))

        productCommentAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductComment> {
            override fun click(position: Int, data: ProductComment, code: Int) {
                when (code) {
                    COMMUNITY_REPLY -> {
                        if (UserDataManager.currentUserId > 0) {
                            val productDetailComment = ProductDetailComment()
                            productDetailComment.product = productDetail
                            productDetailComment.comment = data

                            val extra = Bundle()
                            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(productDetailComment))
                            Navigation.findNavController(view_list_comments).navigate(R.id.action_productDetailFragmentActionBar_to_ratingProductFragment, extra)
                        }
                    }

                    COMMUNITY_REPLY_CHILD -> {
                        if (UserDataManager.currentUserId > 0) {
                            val productDetailComment = ProductDetailComment()
                            productDetailComment.product = productDetail
                            val comment = ProductComment()
                            comment.accountName = data.lastComment?.accountName
                            comment.id = data.id
                            productDetailComment.comment = comment

                            val extra = Bundle()
                            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(productDetailComment))
                            Navigation.findNavController(view_list_comments).navigate(R.id.action_productDetailFragmentActionBar_to_ratingProductFragment)
                        }
                    }

                    COMMUNITY_SHOW_CHILD -> {

                    }

                    COMMUNITY_IMAGE_CLICK -> {
                        val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, data.images!!.toTypedArray())
                        startActivity(intent)
                    }

                    else -> {
                        val intent = Intent(context, MemberProfileActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                        startActivity(intent)
                    }
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

    private fun setupDiaryProducts(context: Context) {
        rv_product_diary.adapter = adapterDiary
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_product_diary.layoutManager = layoutManager
        rv_product_diary.isNestedScrollingEnabled = false

        adapterDiary.listener = object : ClickableAdapter.BaseAdapterAction<DiaryProduct> {
            override fun click(position: Int, data: DiaryProduct, code: Int) {
                when (code) {
                    DIARY_IMAGE_CLICK -> {
                        val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, data.images!!.toTypedArray())
                        startActivity(intent)
                    }

                    DIARY_USER_CLICK -> {
                        val intent = Intent(context, MemberProfileActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.accountId)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Const.RequestCode.PRODUCT_SALE_POINT_DETAIL && resultCode == Activity.RESULT_OK)
            firstLoadSalePoint()

        if (requestCode == Const.RequestCode.SALE_POINT_ADD && resultCode == Activity.RESULT_OK)
            firstLoadSalePoint()
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