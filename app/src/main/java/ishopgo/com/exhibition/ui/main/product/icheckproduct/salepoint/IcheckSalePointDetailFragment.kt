package ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
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
import ishopgo.com.exhibition.domain.response.IcheckSalePointDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductActivity
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductAdapter
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductViewModel
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
    }

    private lateinit var viewModel: IcheckProductViewModel
    private var productId: Long = 0
    private var dataProduct: IcheckProduct? = null
    private var salePoint: IcheckSalePoint? = null
    private var adapter = IcheckProductAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sale_point_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            rv_product_sale_point.adapter = adapter
            rv_product_sale_point.setHasFixedSize(true)
            val layoutManager = GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false)
            rv_product_sale_point.layoutManager = layoutManager
            rv_product_sale_point.isNestedScrollingEnabled = false
            rv_product_sale_point.addItemDecoration(ItemOffsetDecoration(it, R.dimen.item_spacing))

            adapter.listener = object : ClickableAdapter.BaseAdapterAction<IcheckProduct> {
                override fun click(position: Int, data: IcheckProduct, code: Int) {
                    context?.let {
                        val intent = Intent(it, IcheckProductActivity::class.java)
                        intent.putExtra(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(data))
                        it.startActivity(intent)
                    }
                }
            }
        }

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
    }


    @SuppressLint("SetTextI18n")
    private fun showDetail(data: IcheckSalePointDetail) {
        tv_sale_point_name.text = data.name ?: ""
        tv_sale_point_phone.text = data.phone ?: ""
        tv_sale_point_address.text = "${data.address ?: ""}, ${data.district?.name
                ?: ""}, ${data.city?.name
                ?: ""}"
        tv_sale_point_distance.text = "${data.distance ?: 0.0} km"
        linear_footer_call.setOnClickListener {
            val call = Uri.parse("tel:${data.phone}")
            val intent = Intent(Intent.ACTION_DIAL, call)
            if (intent.resolveActivity(it.context.packageManager) != null)
                startActivity(intent)
        }

        linear_footer_message.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "text/plain"
            intent.data = Uri.parse("smsto:${data.phone}")
            dataProduct?.let {
                intent.putExtra("sms_body", "Sản phẩm: ${it.productName ?: ""}\n")
            }
            context?.let {
                if (intent.resolveActivity(it.packageManager) != null)
                    it.startActivity(intent)
            }
        }
    }

    private fun openProductDetail(productId: Long) {
//        val intent = Intent(context, ProductDetailActivity::class.java)
//        intent.putExtra(Const.TransferKey.EXTRA_ID, productId)
//        startActivity(intent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(IcheckProductViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.dataSalePointDetail.observe(this, Observer { p ->
            p?.let { showDetail(it) }
        })

        viewModel.dataProductSalePoint.observe(this, Observer { p ->
            p?.let { adapter.replaceAll(it) }
        })

        val requestSalePoint = String.format("https://gateway.icheck.com.vn/app/local/product/%s/%s", dataProduct?.code
                ?: "", salePoint?.id ?: 0)
        viewModel.getSalePointDetail(requestSalePoint)
        val requestProductSalePoint = String.format("https://gateway.icheck.com.vn/local/products/%s", salePoint?.id
                ?: 0)
        viewModel.loadProductSalePointDetail(requestProductSalePoint)
    }
}