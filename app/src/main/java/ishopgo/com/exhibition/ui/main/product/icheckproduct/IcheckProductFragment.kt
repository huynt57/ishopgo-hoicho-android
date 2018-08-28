package ishopgo.com.exhibition.ui.main.product.icheckproduct

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.JsonSyntaxException
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.*
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.community.ComposingPostMediaAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.extensions.setPhone
import ishopgo.com.exhibition.ui.main.product.detail.ImagesProductFragment
import ishopgo.com.exhibition.ui.main.product.icheckproduct.description.IcheckProductDescriptionActivity
import ishopgo.com.exhibition.ui.main.product.icheckproduct.review.IcheckReviewActivity
import ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint.IcheckSalePointActivity
import ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint.IcheckSalePointAddActivity
import ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint.IcheckSalePointDetailActivity
import ishopgo.com.exhibition.ui.main.product.icheckproduct.shop.IcheckShopActivity
import ishopgo.com.exhibition.ui.main.product.icheckproduct.update.IcheckUpdateProductActivity
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.content_icheck_product_detail.*
import kotlinx.android.synthetic.main.content_icheck_update_product.*

class IcheckProductFragment : BaseFragment() {
//    private var locationManager: LocationManager? = null
//    private var geoLocation = ""
//    override fun onLocationChanged(location: Location?) {
//        geoLocation = location.toString()
//    }

    //    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
//
//    override fun onProviderEnabled(provider: String?) {}
//
//    override fun onProviderDisabled(provider: String?) {}

    private var icheckProduct: IcheckProduct? = null
    private val adapter = IcheckProductAdapter(0.4f)
    private val adapterOnShop = IcheckProductAdapter(0.4f)
    private val adapterSalePoint = IcheckSalePointAdapter()
    private val adapterReview = IcheckReviewAdapter()
    private lateinit var viewModel: IcheckProductViewModel
    private var productCode = ""
    private var productId = -1L
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

    companion object {
        const val TAG = "IcheckProductFragment"
        fun newInstance(params: Bundle): IcheckProductFragment {
            val fragment = IcheckProductFragment()
            fragment.arguments = params

            return fragment
        }

//        private val PERMISSIONS_REQUEST_ACCESS_LOCATION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON) ?: "abc"
        try {
            icheckProduct = Toolbox.gson.fromJson(json, IcheckProduct::class.java)

        } catch (e: JsonSyntaxException) {
            icheckProduct = null
            MaterialDialog.Builder(requireContext())
                    .content("Có lỗi xảy ra!")
                    .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            //            locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            statusCheck()
//            if (ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                activity?.let {
//                    ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSIONS_REQUEST_ACCESS_LOCATION)
//                    ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSIONS_REQUEST_ACCESS_LOCATION)
//                }
//            } else {
//                if (locationManager != null)
//                    locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
//            }


            view_list_products_related.adapter = adapter
            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
            view_list_products_related.layoutManager = layoutManager
            view_list_products_related.isNestedScrollingEnabled = false
            view_list_products_related.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))

            adapter.listener = object : ClickableAdapter.BaseAdapterAction<IcheckProduct> {
                override fun click(position: Int, data: IcheckProduct, code: Int) {
                    context?.let {
                        val intent = Intent(it, IcheckProductActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                        it.startActivity(intent)
                    }
                }
            }


            view_list_products_shop.adapter = adapterOnShop
            val layoutManager4 = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
            view_list_products_shop.layoutManager = layoutManager4
            view_list_products_shop.isNestedScrollingEnabled = false
            view_list_products_shop.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))

            adapterOnShop.listener = object : ClickableAdapter.BaseAdapterAction<IcheckProduct> {
                override fun click(position: Int, data: IcheckProduct, code: Int) {
                    context?.let {
                        val intent = Intent(it, IcheckProductActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                        it.startActivity(intent)
                    }
                }
            }

            val layoutManager2 = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            rv_product_sale_point.layoutManager = layoutManager2
            rv_product_sale_point.adapter = adapterSalePoint
            rv_product_sale_point.addItemDecoration(ItemOffsetDecoration(view.context, R.dimen.item_spacing))
            adapterSalePoint.listener = object : ClickableAdapter.BaseAdapterAction<IcheckSalePoint> {
                override fun click(position: Int, data: IcheckSalePoint, code: Int) {
                    val intent = Intent(it, IcheckSalePointDetailActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                    intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, Toolbox.gson.toJson(icheckProduct))
                    startActivity(intent)
                }
            }

            view_list_comments.adapter = adapterReview
            val layoutManager3 = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            layoutManager3.isAutoMeasureEnabled = true
            view_list_comments.layoutManager = layoutManager3
            view_list_comments.isNestedScrollingEnabled = false
            view_list_comments.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
        }

        view_shop_add_sale_point.setOnClickListener {
            val intent = Intent(context, IcheckSalePointAddActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(icheckProduct))
            startActivityForResult(intent, Const.RequestCode.ICHECK_ADD_SALE_POINT)
        }

        view_product_show_more_sale_point.setOnClickListener {
            val intent = Intent(context, IcheckSalePointActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_ID, productCode)
            intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(icheckProduct))
            startActivity(intent)
        }

        view_product_show_more_comment.setOnClickListener {
            val intent = Intent(context, IcheckReviewActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_ID, icheckProduct?.id ?: 0L)
            startActivity(intent)
        }

    }

//    private fun statusCheck() {
//        val manager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageNoGps()
//        }
//    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Định vị của bạn đang tắt, vui lòng mở lại?")
                .setCancelable(false)
                .setPositiveButton("Đồng ý") { _, _ -> startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton("Huỷ") { dialog, _ ->
                    dialog.cancel()
                }
        val alert = builder.create()
        alert.show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(IcheckProductViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { baseErrorSignal ->
            baseErrorSignal?.let {
                resolveError(it)
            }
        })
        viewModel.detail.observe(this, Observer { data ->
            hideProgressDialog()
            if (data != null) {
                container_description.visibility = View.VISIBLE

                tv_view_details.setOnClickListener {
                    val intent = Intent(context, IcheckProductDescriptionActivity::class.java)
                    intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, data.content)
                    startActivity(intent)
                }

            } else {
                container_description.visibility = View.GONE
            }
        })

        viewModel.dataSalePoint.observe(this, Observer { p ->
            p?.let {
                adapterSalePoint.replaceAll(it.list ?: mutableListOf())
            }
        })

        viewModel.dataReview.observe(this, Observer { p ->
            p?.let {
                adapterReview.replaceAll(it)
            }
        })

        viewModel.dataProductRelated.observe(this, Observer { p ->
            p?.let {
                if (it.isNotEmpty()) {
                    container_products_related.visibility = View.VISIBLE
                    adapter.replaceAll(it)
                } else container_products_related.visibility = View.GONE
            }
        })

        viewModel.dataProductOnShop.observe(this, Observer { p ->
            p?.let {
                if (it.isNotEmpty()) {
                    container_products_shop.visibility = View.VISIBLE
                    adapterOnShop.replaceAll(it)
                } else container_products_shop.visibility = View.GONE
            }
        })

        if (icheckProduct != null) {
            productCode = icheckProduct!!.code ?: ""
            productId = icheckProduct!!.id ?: -1L
            val convert = ProductDetailConverter().convert(icheckProduct!!)
            loadAttributes(icheckProduct!!)
            showData(convert)
        }
    }

    private fun loadAttributes(product: IcheckProduct) {
        loadRelated()
        loadReview()
        loadSalePoint()

        if (product.attributes == null || product.attributes!!.isEmpty()) {
            container_description.visibility = View.GONE
            return
        }
        val attribute = product.attributes!![0]
        container_description.visibility = View.VISIBLE

        tv_description.text = attribute.shortContent

        val request = String.format("https://ishopgo.icheck.com.vn/products/%s/informations/%s", productCode, attribute.id.toString())
        viewModel.loadDetail(request)

    }

    private fun loadRelated() {
        val requestProductRelated = String.format("https://core.icheck.com.vn/products/hooks/prod:%s", productCode)
        viewModel.loadProductRelated(requestProductRelated)
    }

    private fun loadReview() {
        val requestReview = String.format("https://core.icheck.com.vn/reviews?object_id=%s&limit=%s", productId, 5)
        viewModel.loadIcheckReview(requestReview)
    }

    private fun loadSalePoint() {
        val page = 1
        val pageSize = 3
        val requestSalePoint = String.format("https://gateway.icheck.com.vn/app/locations/%s?geo=%s&page=%s&page_size=%s", productCode, "21.735235,121.850293", page, pageSize)
        viewModel.loadSalePoint(requestSalePoint)
    }

    @SuppressLint("SetTextI18n")
    private fun showData(product: ProductDetailProvider) {

        view_rating.rating = product.provideProductCountStar()

        if (product.provideProductImage().isNotEmpty()) {
            val listImages = mutableListOf<String>()
            listImages.add(product.provideProductImage())
            showBanners(listImages)
        }

        if (!TextUtils.isEmpty(product.provideProductName())) view_product_name.text = product.provideProductName()

        if (product.provideProductBarCode().isNotEmpty()) {
            view_product_msp.visibility = View.VISIBLE
            view_product_msp.text = product.provideProductBarCode()
        }

        if (product.provideProductPrice().isNotEmpty()) {
            view_product_price.visibility = View.VISIBLE
            view_product_price.text = product.provideProductPrice()
        }

        val vendor = product.provideProductVendor()
        if (vendor != null) {
            container_shop_info.visibility = View.VISIBLE
            view_shop_name.text = vendor.name
            tv_shop_address.text = vendor.address
            tv_shop_phone.setPhone(vendor.phone ?: "", vendor.phone ?: "")
            view_shop_product_count.text = "${vendor.productCount ?: "0"} Sản phẩm"
            view_shop_rating.text = "${vendor.star ?: "0"} Đánh giá"
            view_product_count.text = "${vendor.productCount ?: "0"} Đánh giá"

            val isVerify = vendor.isVerify ?: false
            view_product_verify.text = if (isVerify) "Đã xác thực" else "Chưa xác thực"

            view_shop_detail.setOnClickListener {
                val intent = Intent(context, IcheckShopActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_ID, vendor.id)
                startActivity(intent)
            }

            val requestOnShop = String.format("https://core.icheck.com.vn/vendors/%s/products", vendor.id, 5)
            viewModel.loadProductOnShop(requestOnShop)
        } else
            container_shop_info.visibility = View.GONE
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

    fun openUpdateProduct() {
        val intent = Intent(context, IcheckUpdateProductActivity::class.java)
        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(icheckProduct))
        startActivityForResult(intent, Const.RequestCode.ICHECK_UPDATE_PRODUCT)
    }
//
//    @SuppressLint("MissingPermission")
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            PERMISSIONS_REQUEST_ACCESS_LOCATION -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (locationManager != null) {
//                        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
//                    }
//                }
//                return
//            }
//
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.RequestCode.ICHECK_ADD_SALE_POINT && resultCode == Activity.RESULT_OK)
            loadSalePoint()
    }

    interface ProductDetailProvider {

        fun provideProductImage(): String
        fun provideProductName(): CharSequence
        fun provideProductPrice(): CharSequence
        fun provideProductBarCode(): CharSequence
        fun provideProductCountLike(): CharSequence
        fun provideProductCountComment(): CharSequence
        fun provideProductCountShare(): CharSequence
        fun provideProductCountStar(): Float
        fun provideProductVendor(): IcheckVendor?
    }

    class ProductDetailConverter : Converter<IcheckProduct, ProductDetailProvider> {

        override fun convert(from: IcheckProduct): ProductDetailProvider {
            return object : ProductDetailProvider {
                override fun provideProductCountComment(): CharSequence {
                    return "${from.commentCount ?: 0} bình luận"
                }

                override fun provideProductCountStar(): Float {
                    return from.star ?: 0.0F
                }

                override fun provideProductCountLike(): CharSequence {
                    return "${from.likeCount ?: 0} thích"
                }

                override fun provideProductCountShare(): CharSequence {
                    return "${from.sellerCount ?: 0} chia sẻ"
                }

                override fun provideProductImage(): String {
                    return "http://ucontent.icheck.vn/" + from.imageDefault + "_medium.jpg"
                }

                override fun provideProductName(): CharSequence {
                    return from.productName ?: ""
                }

                override fun provideProductPrice(): CharSequence {
                    return if (from.priceDefault == 0L) {
                        "<b>Giá bán: <font color=\"#00c853\">Liên hệ</font></b>".asHtml()
                    } else
                        "<b>Giá bán: <font color=\"#00c853\">${from.priceDefault.asMoney()}</font></b>".asHtml()

                }

                override fun provideProductBarCode(): CharSequence {
                    return if (from.code.isNullOrBlank()) ""
                    else "<b>Mã sản phẩm: <font color=\"red\">${from.code}</font></b>".asHtml()
                }

                override fun provideProductVendor(): IcheckVendor? {
                    return from.vendor
                }

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_icheck_product_detail, container, false)
    }
}