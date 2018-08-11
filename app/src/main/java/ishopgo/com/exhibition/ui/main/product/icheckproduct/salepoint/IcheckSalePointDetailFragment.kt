package ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IcheckProduct
import ishopgo.com.exhibition.domain.response.IcheckSalePoint
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_sale_point_detail.*

class IcheckSalePointDetailFragment : BaseFragment() {

    companion object {
        const val TAG = "IcheckSalePointDetailFragment"
        fun newInstance(params: Bundle): IcheckSalePointDetailFragment {
            val fragment = IcheckSalePointDetailFragment()
            fragment.arguments = params

            return fragment
        }

        val PRODUCT_CURRENT = true
        val PRODUCT_NOT_CURRENT = false
        const val DELETE_PRODUCT = 1
    }

    //    private lateinit var salePointViewModel: SalePointShareViewModel
//    private lateinit var viewModel: SalePointDetailViewModel
//    private lateinit var productsAdapter: ClickableAdapter<Product>
    private var phone: String = ""
    private var productId: Long = 0
    private var dataProduct: IcheckProduct? = null
    private var salePoint: IcheckSalePoint? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sale_point_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            rv_product_sale_point.setHasFixedSize(true)
            val layoutManager = GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false)
            rv_product_sale_point.layoutManager = layoutManager
            rv_product_sale_point.isNestedScrollingEnabled = false
            rv_product_sale_point.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))
        }
        if (salePoint != null)
            showDetail(salePoint!!)

        if (dataProduct != null) {
            linear_product_current.visibility = View.VISIBLE
            Glide.with(context).load("http://ucontent.icheck.vn/" + dataProduct!!.imageDefault + "_medium.jpg")
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(img_product)
            tv_product.text = dataProduct!!.productName
            tv_product_price.text = dataProduct!!.priceDefault.asMoney()
            tv_product_code.text = dataProduct!!.code

        } else linear_product_current.visibility = View.GONE

        tv_product_detail.setOnClickListener { openProductDetail(productId) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_REQUIRE)
        dataProduct = Toolbox.gson.fromJson(json, IcheckProduct::class.java)

        val json2 = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        salePoint = Toolbox.gson.fromJson(json2, IcheckSalePoint::class.java)
//        phone = arguments?.getString(Const.TransferKey.EXTRA_REQUIRE, "") ?: ""

//        productsAdapter = if (UserDataManager.currentUserPhone == phone) SalePointProductAdapter() else
//            ProductAdapter()
    }

    private fun openActivtyLogin() {
//        val intent = Intent(context, LoginActivity::class.java)
//        intent.putExtra(Const.TransferKey.EXTRA_REQUIRE, true)
//        startActivity(intent)
    }


    private fun showDetail(data: IcheckSalePoint) {
        if (data != null) {
            tv_sale_point_name.text = data.name ?: ""
//            tv_sale_point_phone.text = data.phone ?: ""
            tv_sale_point_address.text = data.address ?: ""
            tv_sale_point_distance.text = "${data.distance ?: 0.0} km"
            linear_footer_call.setOnClickListener {
//                val call = Uri.parse("tel:${data.phone}")
                val call = Uri.parse("tel: 0989013403")
                val intent = Intent(Intent.ACTION_DIAL, call)
                if (intent.resolveActivity(it.context.packageManager) != null)
                    startActivity(intent)
            }

            linear_footer_message.setOnClickListener {
                if (UserDataManager.currentUserId <= 0) {
                    openActivtyLogin()
                    return@setOnClickListener
                }
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.type = "text/plain"
//                intent.data = Uri.parse("smsto:${data.phone}")
                intent.data = Uri.parse("smsto: 0989013403")
                dataProduct?.let {
                    intent.putExtra("sms_body", "Sản phẩm: ${it.productName ?: ""}\n")
                }
                context?.let {
                    if (intent.resolveActivity(it.packageManager) != null)
                        it.startActivity(intent)
                }
            }
        }

//        if (data.products != null) {
//            context?.let {
//                val product = data.products!!
//                product.data?.let { productsAdapter.replaceAll(it) }
//                rv_product_sale_point.adapter = productsAdapter
//                productsAdapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
//                    override fun click(position: Int, data: Product, code: Int) {
//                        when (code) {
//                            DELETE_PRODUCT -> {
//                                dialogDeleteProduct(data.id, PRODUCT_NOT_CURRENT)
//                            }
//                            else -> context?.let {
//                                productId = data.id
//
//                                val productDetail = ProductDetail()
//                                productDetail.image = data.image
//                                productDetail.name = data.name
//                                productDetail.price = data.price
//                                productDetail.code = data.code
//                                dataProduct = productDetail
//                                viewModel.loadData(phone, productId)
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    private fun openProductDetail(productId: Long) {
//        val intent = Intent(context, ProductDetailActivity::class.java)
//        intent.putExtra(Const.TransferKey.EXTRA_ID, productId)
//        startActivity(intent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        salePointViewModel = obtainViewModel(SalePointShareViewModel::class.java, true)
//
//        viewModel = obtainViewModel(SalePointDetailViewModel::class.java, false)
//        viewModel.errorSignal.observe(this, Observer {
//            it?.let {
//                hideProgressDialog()
//                resolveError(it)
//            }
//        })
//        viewModel.conversation.observe(this, Observer { c ->
//            c?.let {
//                val conv = LocalConversationItem()
//                conv.idConversions = c.id ?: ""
//                conv.name = c.name ?: ""
//
//                context?.let {
//                    val intent = Intent(it, ConversationActivity::class.java)
//                    intent.putExtra(Const.TransferKey.EXTRA_CONVERSATION_ID, conv.idConversions)
//                    intent.putExtra(Const.TransferKey.EXTRA_TITLE, conv.name)
//                    startActivity(intent)
//                }
//            }
//        })
//
//        viewModel.getData.observe(this, Observer { p ->
//            p.let {
//                hideProgressDialog()
//                it?.let { it1 -> showDetail(it1) }
//            }
//        })

//        if (dataProduct != null) {
//            productId = dataProduct!!.id
//        }

//        viewModel.loadData(phone, productId)
    }
}