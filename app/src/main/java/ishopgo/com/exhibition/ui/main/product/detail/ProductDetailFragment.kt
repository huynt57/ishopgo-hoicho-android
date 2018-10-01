package ishopgo.com.exhibition.ui.main.product.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import ishopgo.com.exhibition.domain.request.ListBGBNRequest
import ishopgo.com.exhibition.domain.request.ProductDiaryRequest
import ishopgo.com.exhibition.domain.request.ProductSalePointRequest
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.model.diary.DiaryProduct
import ishopgo.com.exhibition.ui.base.BackpressConsumable
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationActivity
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileActivity
import ishopgo.com.exhibition.ui.extensions.*
import ishopgo.com.exhibition.ui.main.MainActivity
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.branded.ProductsOfBrandActivity
import ishopgo.com.exhibition.ui.main.product.detail.add_sale_point.ProductSalePointAddActivity
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentAdapter
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentsActivity
import ishopgo.com.exhibition.ui.main.product.detail.description.DescriptionActivity
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

    private val listImage = mutableListOf<String>()
    private val sameShopProductsAdapter = ProductAdapter(0.4f)
    private val viewedProductAdapter = ProductAdapter(0.4f)
    private val favoriteProductAdapter = ProductAdapter(0.4f)
    private val productCommentAdapter = ProductCommentAdapter()
    private val productProcessAdapter = ProductProcessAdapter()
    private var adapterSalePoint = ProductSalePointAdapter()
    private var adapterDiary = ProductDiaryAdapter()
    private var adapterCert = ProductCertAdapter()
    private var adapterDescriptionCSCB = ListDescriptionAdapter()
    private var adapterDescriptionVatTu = ListDescriptionAdapter()
    private var adapterDescriptionGiaiPhap = ListDescriptionAdapter()
    private val vatTuProductAdapter = ProductAdapter(0.4f)
    private val giaiPhapProductAdapter = ProductAdapter(0.4f)
    private val relatedProductAdapter = ProductAdapter(0.4f)

    private var isViewDiary = false
    private var productId: Long = -1L
    private var stampId = ""
    private var stampCode = ""
    private var stampType = ""
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
        stampId = arguments?.getString(Const.TransferKey.EXTRA_STAMP_ID, "") ?: ""
        stampCode = arguments?.getString(Const.TransferKey.EXTRA_STAMP_CODE, "") ?: ""
        stampType = arguments?.getString(Const.TransferKey.EXTRA_STAMP_TYPE, "") ?: ""

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
        firstLoad.limit = 1
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
            p?.let {
                if (it.isEmpty())
                    if (isViewDiary)
                        container_diary.visibility = View.VISIBLE
                    else
                        container_diary.visibility = View.GONE
                else {
                    container_diary.visibility = View.VISIBLE
                    it.let { it1 -> adapterDiary.replaceAll(it1) }
                }
            }
        })

        loadData(productId)
        firstLoadSalePoint()
    }

    var productDetail = ProductDetail()

    @SuppressLint("SetTextI18n")
    private fun showProductDetail(product: ProductDetail) {
        context?.let {
            productDetail = product

            if (productDetail.certImages?.isNotEmpty() == true) {
                val listCert = productDetail.certImages!!
                container_cert.visibility = View.VISIBLE
                for (i in listCert.indices)
                    listImage.add(listCert[i].image ?: "")

                adapterCert.replaceAll(listCert)
            } else container_cert.visibility = View.GONE

            val processes = product.process ?: listOf()
            container_product_process.visibility = if (processes.isEmpty()) View.GONE else View.VISIBLE
            productProcessAdapter.replaceAll(processes)


            val convert = ProductDetailConverter().convert(product)

            if (convert.providerStamp() != null) {
                val stamp = convert.providerStamp()!!
                const_vertify.visibility = View.VISIBLE
                if (stamp.isFake == true) {
                    tv_not_vertify.visibility = View.VISIBLE
                    tv_vertify.visibility = View.GONE

                } else {
                    tv_vertify.visibility = View.VISIBLE
                    tv_not_vertify.visibility = View.GONE
                }

                if (stamp.code?.isNotEmpty() == true)
                    tv_code.text = "Serial: ${stamp.code}"

                if (stamp.numberOfScans != null)
                    tv_number_scan.text = "${stamp.numberOfScans} Số lần quét"
                else linear_stamp_vertify.visibility = View.GONE
                if (stamp.numberOfScanners != null)
                    tv_user_scan.text = "${stamp.numberOfScanners} Số người quét"
                else linear_stamp_vertify.visibility = View.GONE

            } else const_vertify.visibility = View.GONE

            view_product_wholesale.visibility = View.VISIBLE
            view_product_wholesale.text = convert.provideWholesale()

            if (product.wholesalePriceFrom == 0L || product.wholesalePriceTo == 0L)
                view_product_wholesale_limit.visibility = View.GONE
            else if (convert.provideWholesaleLimit().isNotEmpty()) {
                view_product_wholesale_limit.visibility = View.VISIBLE
                view_product_wholesale_limit.text = convert.provideWholesaleLimit()
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

            if (convert.provideProductCode().isNotEmpty()) {
                view_product_msp.visibility = View.VISIBLE
                view_product_msp.text = convert.provideProductCode()
            }
            if (convert.providePackaging().isNotEmpty()) {
                view_product_packaging.visibility = View.VISIBLE
                view_product_packaging.text = convert.providePackaging()
            }

            if (convert.provideDVT().isNotEmpty()) {
                view_product_dvt.visibility = View.VISIBLE
                view_product_dvt.text = convert.provideDVT()
            }

//            if (convert.provideHSD().isNotEmpty()) {
//                view_product_hsd.visibility = View.VISIBLE
//                view_product_hsd.text = convert.provideHSD()
//            }
//
//            if (convert.provideNgayDongGoi().isNotEmpty()) {
//                view_product_ngayDongGoi.visibility = View.VISIBLE
//                view_product_ngayDongGoi.text = convert.provideNgayDongGoi()
//            }
//
//            if (convert.provideNoCodeSX().isNotEmpty()) {
//                view_product_maSanXuat.visibility = View.VISIBLE
//                view_product_maSanXuat.text = convert.provideNoCodeSX()
//            }
//
//            if (convert.provideDateProduce().isNotEmpty()) {
//                view_product_ngaySanXuat.visibility = View.VISIBLE
//                view_product_ngaySanXuat.text = convert.provideDateProduce()
//            }
//
//            if (convert.provideDateExpected().isNotEmpty()) {
//                view_product_ngayThuHoachDK.visibility = View.VISIBLE
//                view_product_ngayThuHoachDK.text = convert.provideDateExpected()
//            }

            if (convert.provideScale().isNotEmpty()) {
                view_product_quyMoSanXuat.visibility = View.VISIBLE
                view_product_quyMoSanXuat.text = convert.provideScale()
            }

            if (convert.provideNumberExpected().isNotEmpty()) {
                view_product_date_khaNangCungUng.visibility = View.VISIBLE
                view_product_date_khaNangCungUng.text = convert.provideNumberExpected()
            }

            if (convert.provideSeason().isNotEmpty()) {
                view_product_date_muaVu.visibility = View.VISIBLE
                view_product_date_muaVu.text = convert.provideSeason()
            }

//            if (view_product_maSanXuat.visibility == View.VISIBLE || view_product_ngaySanXuat.visibility == View.VISIBLE ||
//                    view_product_ngayThuHoachDK.visibility == View.VISIBLE || view_product_quyMoSanXuat.visibility == View.VISIBLE ||
//                    view_product_date_khaNangCungUng.visibility == View.VISIBLE || view_product_date_muaVu.visibility == View.VISIBLE)
//                linear_sanXuat.visibility = View.VISIBLE
//            else linear_sanXuat.visibility = View.GONE
//
//            if (convert.providerHinhThucVC().isNotEmpty()) {
//                view_product_hinhThucVC.visibility = View.VISIBLE
//                view_product_hinhThucVC.text = convert.providerHinhThucVC()
//            }
//
//            if (convert.providerNgayVC().isNotEmpty()) {
//                view_product_ngayVC.visibility = View.VISIBLE
//                view_product_ngayVC.text = convert.providerNgayVC()
//            }
//
//            if (convert.providerDonViVC().isNotEmpty()) {
//                view_product_tenDonViVC.visibility = View.VISIBLE
//                view_product_tenDonViVC.text = convert.providerDonViVC()
//            }
//
//            if (convert.providerNoteVC().isNotEmpty()) {
//                view_product_ghiChuVC.visibility = View.VISIBLE
//                view_product_ghiChuVC.text = convert.providerNoteVC()
//            }
//
//            if (view_product_hinhThucVC.visibility == View.VISIBLE || view_product_hinhThucVC.visibility == View.VISIBLE ||
//                    view_product_ngayVC.visibility == View.VISIBLE || view_product_tenDonViVC.visibility == View.VISIBLE ||
//                    view_product_ghiChuVC.visibility == View.VISIBLE)
//                linear_vanChuyen.visibility = View.VISIBLE
//            else linear_vanChuyen.visibility = View.GONE

            if (convert.providerExchangeDiaryProduct().isNotEmpty()){

            }

            view_product_brand.visibility = if (convert.provideProductBrand().isBlank()) View.GONE else View.VISIBLE
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

            if (convert.providerDVPP() != null && convert.providerDVPP()?.id != 0L) {
                val dvpp = convert.providerDVPP()
                container_shop_dvpp.visibility = View.VISIBLE
                view_dvpp_name.text = dvpp?.name
                view_dvpp_product_count.text = "<b><font color=\"#00c853\">${dvpp?.count
                        ?: 0}</font></b><br>Sản phẩm".asHtml()
                view_dvpp_rating.text = "<b><font color=\"red\">${dvpp?.rate?.toFloat()
                        ?: 0.0f}/5.0</font></b><br>${dvpp?.rateCount
                        ?: 0} Đánh giá".asHtml()
                tv_dvpp_phone.setPhone(dvpp?.hotline ?: "", product.pp?.hotline
                        ?: "")
                tv_dvpp_address.text = dvpp?.address ?: ""
                view_dvpp_detail.setOnClickListener {
                    openShopDetail(it.context, dvpp?.id ?: 0L)
                }
            }

            if (convert.providerCSCB() != null && convert.providerCSCB()?.id != 0L) {
                val cscb = convert.providerCSCB()
                container_shop_cscb.visibility = View.VISIBLE
                view_cscb_name.text = cscb?.name
                view_cscb_product_count.text = "<b><font color=\"#00c853\">${cscb?.count
                        ?: 0}</font></b><br>Sản phẩm".asHtml()
                view_cscb_rating.text = "<b><font color=\"red\">${cscb?.rate?.toFloat()
                        ?: 0.0f}/5.0</font></b><br>${cscb?.rateCount
                        ?: 0} Đánh giá".asHtml()
                tv_cscb_phone.setPhone(cscb?.hotline ?: "", product.cscb?.hotline
                        ?: "")
                tv_cscb_address.text = cscb?.address ?: ""
                view_cscb_detail.setOnClickListener {
                    openShopDetail(it.context, cscb?.id ?: 0L)
                }

                if (cscb?.descriptions?.isNotEmpty() == true) {
                    rv_description_cscb.visibility = View.VISIBLE
                    adapterDescriptionCSCB.replaceAll(cscb.descriptions!!)
                } else rv_description_cscb.visibility = View.GONE

            }

            if (convert.providerDVNK() != null && convert.providerDVNK()?.id != 0L) {
                val dvnk = convert.providerDVNK()
                container_shop_dvnk.visibility = View.VISIBLE
                view_dvnk_name.text = dvnk?.name
                view_dvnk_product_count.text = "<b><font color=\"#00c853\">${dvnk?.count
                        ?: 0}</font></b><br>Sản phẩm".asHtml()
                view_dvnk_rating.text = "<b><font color=\"red\">${dvnk?.rate?.toFloat()
                        ?: 0.0f}/5.0</font></b><br>${dvnk?.rateCount
                        ?: 0} Đánh giá".asHtml()
                tv_dvnk_phone.setPhone(dvnk?.hotline ?: "", product.nnk?.hotline
                        ?: "")
                tv_dvnk_address.text = dvnk?.address ?: ""
                view_dvnk_detail.setOnClickListener {
                    openShopDetail(it.context, dvnk?.id ?: 0L)
                }
            }

            for (i in convert.providerInfo().indices) {
                val data = convert.providerInfo()[i]
                if (i == 0) {
                    if (data.products?.data?.isNotEmpty() == false && data.descriptions?.isNotEmpty() == false)
                        container_nguyenLieu_vatTu.visibility = View.GONE
                    else {
                        container_nguyenLieu_vatTu.visibility = View.VISIBLE
                        label_nguyenLieu_vatTu.text = data.name
                        if (data.descriptions?.isNotEmpty() == true) {
                            view_list_desc_nguyenLieu_vatTu.visibility = View.VISIBLE
                            adapterDescriptionVatTu.replaceAll(data.descriptions ?: mutableListOf())
                        } else view_list_desc_nguyenLieu_vatTu.visibility = View.GONE

                        if (data.products?.data?.isNotEmpty() == true) {
                            view_list_products_nguyenLieu_vatTu.visibility = View.VISIBLE
                            vatTuProductAdapter.replaceAll(data.products?.data ?: mutableListOf())
                        } else view_list_products_nguyenLieu_vatTu.visibility = View.GONE
                    }
                }
                if (i == 1) {
                    if (data.products?.data?.isNotEmpty() == false && data.descriptions?.isNotEmpty() == false)
                        container_giaiPhap.visibility = View.GONE
                    else {
                        container_giaiPhap.visibility = View.VISIBLE
                        label_giaiPhap.text = data.name
                        if (data.descriptions?.isNotEmpty() == true) {
                            view_list_desc_giaiPhap.visibility = View.VISIBLE
                            adapterDescriptionGiaiPhap.replaceAll(data.descriptions
                                    ?: mutableListOf())
                        } else view_list_desc_giaiPhap.visibility = View.VISIBLE

                        if (data.products?.data?.isNotEmpty() == true) {
                            view_list_products_giaiPhap.visibility = View.VISIBLE
                            giaiPhapProductAdapter.replaceAll(data.products?.data
                                    ?: mutableListOf())
                        } else view_list_products_giaiPhap.visibility = View.GONE
                    }
                }
                if (i == 2) {
                    if (data.products?.data?.isNotEmpty() == true) {
                        container_spLienQuan.visibility = View.VISIBLE
                        label_spLienQuan.text = data.name
                        relatedProductAdapter.replaceAll(data.products?.data ?: mutableListOf())
                    } else container_spLienQuan.visibility = View.GONE
                }
            }

            view_product_description.loadData(productDesc, "text/html", null)
            view_label_shop_name.text = convert.provideShopLabel()
            view_shop_name.text = convert.provideShopName()
            view_shop_product_count.text = convert.provideShopProductCount()
            view_shop_rating.text = convert.provideShopRatePoint()
            view_product_like_count.text = "${convert.provideProductLikeCount()} thích"
            view_product_comment_count.text = "${convert.provideProductCommentCount()} Đánh giá"
            view_product_share_count.text = "${convert.provideProductShareCount()} chia sẻ"
            tv_shop_phone.setPhone(convert.provideShopPhone(), product.booth?.hotline ?: "")

            tv_shop_address.text = convert.provideShopAddress()
            view_shop_detail.setOnClickListener {
                openShopDetail(it.context, product.booth?.id ?: 0L)
            }
            view_shop_call.setOnClickListener { callShop(it.context, product) }
            view_shop_message.setOnClickListener { messageShop(it.context, product) }
            view_product_show_more_description.setOnClickListener { showProductFullDescription(it.context, product) }
            view_product_show_more_comment.setOnClickListener { showMoreComment(it.context, product) }
            view_product_show_more_sale_point.setOnClickListener { showMoreSalePoint(it.context, product) }
            more_products_same_shop.setOnClickListener { openProductsOfShop(it.context, product) }
            more_favorite.setOnClickListener { openFavoriteProducts(it.context) }
            more_viewed.setOnClickListener { openViewedProducts(it.context) }
            view_product_brand.setOnClickListener {
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


                if (UserDataManager.currentUserId == product.booth?.id) {
                    isViewDiary = true
                    view_add_diary.visibility = View.VISIBLE
                } else {
                    isViewDiary = false
                    view_add_diary.visibility = View.GONE
                }

                firstLoadDiary()

            } else container_diary.visibility = View.GONE

            if (stampType.isNotEmpty()) {
                container_exchange_diary.visibility = View.VISIBLE

                if (UserDataManager.currentType == "Quản trị viên") {
                    val listPermission = Const.listPermission
                    if (listPermission.isNotEmpty())
                        for (i in listPermission.indices)
                            if (Const.Permission.EXPO_BOOTH_PRODUCTION_DIARY_ADD == listPermission[i]) {
                                view_add_exchange_diary.visibility = View.VISIBLE
                                break
                            }
                } else view_add_exchange_diary.visibility = View.GONE


                if (UserDataManager.currentUserId == product.booth?.id || UserDataManager.currentType == "Chủ hội chợ") {
                    isViewDiary = true
                    view_add_exchange_diary.visibility = View.VISIBLE
                } else {
                    isViewDiary = false
                    view_add_exchange_diary.visibility = View.GONE
                }

                view_add_exchange_diary.setOnClickListener { showAddExchangDiary(product) }
            } else container_exchange_diary.visibility = View.GONE

            openProductSalePoint(product)

            floatQrCode.setOnClickListener {
                showQrCodeProduct(product)
            }
        }
    }

    private fun showQrCodeProduct(product: ProductDetail) {
        val extra = Bundle()
        extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))

        Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_productDetailFragmentActionBar_to_qrCodeProductFragment, extra)
    }

    private fun showAddExchangDiary(product: ProductDetail) {
        val extra = Bundle()
        extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(product))

        Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_productDetailFragmentActionBar_to_productExchangeDiaryAddFragment, extra)
    }

    private fun setupRecyclerviewDescriptionCSCB() {
        context?.let {
            rv_description_cscb.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_description_cscb.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_description_cscb.adapter = adapterDescriptionCSCB
        }
    }

    private fun setupRecyclerviewDescriptionVatTu() {
        context?.let {
            view_list_desc_nguyenLieu_vatTu.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            view_list_desc_nguyenLieu_vatTu.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            view_list_desc_nguyenLieu_vatTu.adapter = adapterDescriptionVatTu
        }
    }

    private fun setupRecyclerviewDescriptionGiaiPhap() {
        context?.let {
            view_list_desc_giaiPhap.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            view_list_desc_giaiPhap.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            view_list_desc_giaiPhap.adapter = adapterDescriptionGiaiPhap
        }
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

        fun provideDVT(): CharSequence
        fun provideHSD(): CharSequence
        fun provideNgayDongGoi(): CharSequence


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
        fun provideNoCodeSX(): CharSequence
        fun provideDateProduce(): CharSequence
        fun provideDateExpected(): CharSequence
        fun provideScale(): CharSequence
        fun provideNumberExpected(): CharSequence
        fun provideSeason(): CharSequence
        fun provideIsDiary(): Boolean

        fun providerHinhThucVC(): CharSequence
        fun providerNgayVC(): CharSequence
        fun providerDonViVC(): CharSequence
        fun providerNoteVC(): CharSequence

        fun providerDVPP(): Booth?
        fun providerDVNK(): Booth?
        fun providerCSCB(): Booth?
        fun providerInfo(): List<InfoProduct>
        fun providerStamp(): ProductDetail.Stamp?
        fun providerExchangeDiaryProduct(): List<ExchangeDiaryProduct>
    }

    class ProductDetailConverter : Converter<ProductDetail, ProductDetailProvider> {

        override fun convert(from: ProductDetail): ProductDetailProvider {
            return object : ProductDetailProvider {
                override fun providerExchangeDiaryProduct(): List<ExchangeDiaryProduct> {
                    return from.exchangeDiaryProduct ?: mutableListOf()
                }

                override fun providerStamp(): ProductDetail.Stamp? {
                    return from.stamp
                }

                override fun providerInfo(): List<InfoProduct> {
                    return from.info ?: mutableListOf()
                }

                override fun providerDVPP(): Booth? {
                    return from.pp
                }

                override fun providerDVNK(): Booth? {
                    return from.nnk
                }

                override fun providerCSCB(): Booth? {
                    return from.cscb
                }

                override fun provideDVT(): CharSequence {
                    return if (from.dvt.isNullOrBlank()) ""
                    else "<b>Đơn vị tính: <font color=\"red\">${from.dvt}</font></b>".asHtml()
                }

                override fun provideHSD(): CharSequence {
                    return if (from.hsd.isNullOrBlank()) ""
                    else "<b>Hạn sử dụng: <font color=\"red\">${from.hsd?.asDateProdcutDetail()}</font></b>".asHtml()
                }

                override fun provideNgayDongGoi(): CharSequence {
                    return if (from.ngayDonggoi.isNullOrBlank()) ""
                    else "<b>Ngày đóng gói: <font color=\"red\">${from.ngayDonggoi?.asDateProdcutDetail()}</font></b>".asHtml()
                }

                override fun providerHinhThucVC(): CharSequence {
                    return if (from.hinhThucVc.isNullOrBlank()) ""
                    else "<b>Hình thức vận chuyển: <font color=\"red\">${from.hinhThucVc}</font></b>".asHtml()
                }

                override fun providerNgayVC(): CharSequence {
                    return if (from.ngayVc.isNullOrBlank()) ""
                    else "<b>Ngày vận chuyển: <font color=\"red\">${from.ngayVc}</font></b>".asHtml()
                }

                override fun providerDonViVC(): CharSequence {
                    return if (from.donviVc.isNullOrBlank()) ""
                    else "<b>Đơn vị vận chuyển: <font color=\"red\">${from.donviVc}</font></b>".asHtml()
                }

                override fun providerNoteVC(): CharSequence {
                    return if (from.vcNote.isNullOrBlank()) ""
                    else "<b>Ghi chú vận chuyển: <font color=\"red\">${from.vcNote}</font></b>".asHtml()
                }

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
                    return if (from.booth?.title.isNullOrEmpty())
                        "Gian hàng trưng bày"
                    else from.booth?.title!!
                }

                override fun provideMadeIn(): CharSequence {
                    return if (from.madeIn.isNullOrBlank()) ""
                    else "<b>Xuất xứ: <font color=\"red\">${from.madeIn}</font></b>".asHtml()
                }

                override fun provideNoCodeSX(): CharSequence {
                    return if (from.msSanxuat.isNullOrBlank()) ""
                    else "<b>Mã số lô sản xuất: <font color=\"red\">${from.msSanxuat}</font></b>".asHtml()
                }

                override fun provideDateProduce(): CharSequence {
                    return if (from.ngaySx.isNullOrBlank()) ""
                    else "<b>Ngày bắt đầu sản xuất: <font color=\"red\">${from.ngaySx?.asDateProdcutDetail()}</font></b>".asHtml()
                }

                override fun provideDateExpected(): CharSequence {
                    return if (from.dkThuhoach.isNullOrBlank()) ""
                    else "<b>Ngày thu hoạch dự kiến: <font color=\"red\">${from.dkThuhoach?.asDateProdcutDetail()}</font></b>".asHtml()
                }

                override fun provideScale(): CharSequence {
                    return if (from.quyMo.isNullOrBlank()) ""
                    else "<b>Quy mô sản xuất: <font color=\"red\">${from.quyMo}</font></b>".asHtml()
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
                    return if (from.wholesaleCountProduct?.isNotEmpty() == true) "<b>Mua tối thiểu <font color=\"red\">${from.wholesaleCountProduct}</font></b>".asHtml()
                    else ""
                }

                override fun provideWholesale(): CharSequence {
                    return if (from.wholesalePriceFrom == 0L || from.wholesalePriceTo == 0L) "<b>Giá bán sỉ: <font color=\"red\">Liên hệ</font></b>".asHtml()
                    else "<b>Giá bán sỉ: Từ <font color=\"red\">${from.wholesalePriceFrom.asMoney()}</font> tới <font color=\"red\">${from.wholesalePriceTo.asMoney()}</font></b>".asHtml()
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
                    return if (from.price == 0L) {
                        "<b>Giá bán lẻ: <font color=\"#00c853\">Liên hệ</font></b>".asHtml()
                    } else if (from.promotionPrice != null && from.promotionPrice != from.price) {
                        if (from.promotionPrice == 0L) // gia khuyen mai = 0 thi coi nhu ko khuyen mai
                            "<b>Giá bán lẻ: <font color=\"#00c853\">${from.price.asMoney()}</font></b>".asHtml()
                        else
                            "<b>Giá bán lẻ: <font color=\"#BDBDBD\"><strike>${from.price.asMoney()}</strike></font> <font color=\"#00c853\">${from.promotionPrice.asMoney()}</font></b> ".asHtml()
                    } else {
                        "<b>Giá bán lẻ: <font color=\"#00c853\">${from.price.asMoney()}</font></b>".asHtml()
                    }
                }

                override fun provideProductBrand(): CharSequence {
                    if (from.department?.id == 0L) return ""
                    return "<b>Thương hiệu: <font color=\"#f73a04\">${from.department?.name?.trim()
                            ?: ""}</font></b>".asHtml()
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
                    return from.booth?.hotline ?: ""
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
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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
        setupExchangeDiaryProducts(view.context)
        setupProductVatTu(view.context)
        setupProductGiaiPhap(view.context)
        setupProductRelated(view.context)
        setupSalePointRecycleview()
        setupCertRecyclerview()
        setupRecyclerviewDescriptionCSCB()
        setupRecyclerviewDescriptionVatTu()
        setupRecyclerviewDescriptionGiaiPhap()
        setupListeners()

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

    private fun setupCertRecyclerview() {
        context?.let {
            rv_product_cert.adapter = adapterCert
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_product_cert.layoutManager = layoutManager
            rv_product_cert.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
            rv_product_cert.isNestedScrollingEnabled = false
        }
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

        adapterDiary.listener = object : ClickableAdapter.BaseAdapterAction<DiaryProduct> {
            override fun click(position: Int, data: DiaryProduct, code: Int) {
                when (code) {
                    DIARY_IMAGE_CLICK -> {
                        val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                        val listImages = mutableListOf<String>()
                        if (data.images?.isNotEmpty() == true)
                            for (i in data.images!!.indices)
                                listImages.add(data.images!![i].image ?: "")
                        intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, listImages.toTypedArray())
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

        adapterCert.listener = object : ClickableAdapter.BaseAdapterAction<ProductDetail.ListCert> {
            override fun click(position: Int, data: ProductDetail.ListCert, code: Int) {
                context?.let {
                    val intent = Intent(context, PhotoAlbumViewActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, listImage.toTypedArray())
                    startActivity(intent)
                }
            }

        }

        adapterDescriptionCSCB.listener = object : ClickableAdapter.BaseAdapterAction<Description> {
            override fun click(position: Int, data: Description, code: Int) {
                val intent = Intent(context, DescriptionActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                startActivity(intent)
            }
        }

        adapterDescriptionVatTu.listener = object : ClickableAdapter.BaseAdapterAction<Description> {
            override fun click(position: Int, data: Description, code: Int) {
                val intent = Intent(context, DescriptionActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                startActivity(intent)
            }
        }

        adapterDescriptionGiaiPhap.listener = object : ClickableAdapter.BaseAdapterAction<Description> {
            override fun click(position: Int, data: Description, code: Int) {
                val intent = Intent(context, DescriptionActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                startActivity(intent)
            }
        }

        vatTuProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                context?.let { openProductDetail(it, data) }
            }
        }

        giaiPhapProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
            override fun click(position: Int, data: Product, code: Int) {
                context?.let { openProductDetail(it, data) }
            }
        }

        relatedProductAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
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
                startActivityForResult(intent, Const.RequestCode.UPDATE_PRODUCT_SALE_POINT)
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

    private fun openShopDetail(context: Context, boothId: Long) {
        val intent = Intent(context, ShopDetailActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_ID, boothId)
        startActivity(intent)
    }

    @SuppressLint("HardwareIds")
    private fun loadData(productId: Long) {
        val deviceId = if (stampId.isNotEmpty() && stampCode.isNotEmpty()) {
            context?.let {
                var device = Settings.Secure.getString(it.contentResolver, Settings.Secure.ANDROID_ID)
                if (device.isNullOrEmpty()) device = Build.SERIAL
                if (device.isEmpty()) device = Build.DISPLAY
                device
            }
        } else ""

        viewModel.loadData(productId, stampId, stampCode, stampType, deviceId ?: "")

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
    }

    private fun setupExchangeDiaryProducts(context: Context) {
        rv_product_exchange_diary.adapter = adapterDiary
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_product_exchange_diary.layoutManager = layoutManager
        rv_product_exchange_diary.isNestedScrollingEnabled = false
    }

    private fun setupProductVatTu(context: Context) {
        view_list_products_nguyenLieu_vatTu.adapter = vatTuProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_products_nguyenLieu_vatTu.layoutManager = layoutManager
        view_list_products_nguyenLieu_vatTu.isNestedScrollingEnabled = false
        view_list_products_nguyenLieu_vatTu.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupProductGiaiPhap(context: Context) {
        view_list_products_giaiPhap.adapter = giaiPhapProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_products_giaiPhap.layoutManager = layoutManager
        view_list_products_giaiPhap.isNestedScrollingEnabled = false
        view_list_products_giaiPhap.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    private fun setupProductRelated(context: Context) {
        view_list_products_spLienQuan.adapter = relatedProductAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view_list_products_spLienQuan.layoutManager = layoutManager
        view_list_products_spLienQuan.isNestedScrollingEnabled = false
        view_list_products_spLienQuan.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Const.RequestCode.PRODUCT_SALE_POINT_DETAIL && resultCode == Activity.RESULT_OK)
            firstLoadSalePoint()

        if (requestCode == Const.RequestCode.SALE_POINT_ADD && resultCode == Activity.RESULT_OK)
            firstLoadSalePoint()

        if (requestCode == Const.RequestCode.UPDATE_PRODUCT_SALE_POINT && resultCode == Activity.RESULT_OK)
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