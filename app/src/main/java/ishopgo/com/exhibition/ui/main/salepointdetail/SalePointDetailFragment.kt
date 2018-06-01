package ishopgo.com.exhibition.ui.main.salepointdetail

import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.search_sale_point.ManagerSalePointDetail
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.main.product.ProductProvider
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailActivity
import kotlinx.android.synthetic.main.fragment_sale_point_detail.*

class SalePointDetailFragment : BaseFragment() {

    companion object {
        fun newInstance(params: Bundle): SalePointDetailFragment {
            val fragment = SalePointDetailFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private lateinit var viewModel: SalePointDetailViewModel
    private val productsAdapter = ProductAdapter(0.4f)
    private var phone: String = ""
    private var dataProduct: ProductDetail? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sale_point_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        dataProduct = Toolbox.gson.fromJson(json, ProductDetail::class.java)
        phone = arguments?.getString(Const.TransferKey.EXTRA_REQUIRE, "") ?: ""
    }

    private fun showDetail(data: ManagerSalePointDetail) {
        if (data.salePoint != null) {
            val salePoint = data.salePoint
            tv_sale_point_name.text = salePoint?.provideName() ?: ""
            tv_sale_point_phone.text = salePoint?.providePhone() ?: ""
            tv_sale_point_address.text = salePoint?.provideAddress() ?: ""
            linear_footer.setOnClickListener {
                val call = Uri.parse("tel:${salePoint?.providePhone()}")
                val intent = Intent(Intent.ACTION_DIAL, call)
                if (intent.resolveActivity(it.context.packageManager) != null)
                    startActivity(intent)
            }
        }

        if (data.products != null) {
            val product = data.products!!
            product.data?.let { productsAdapter.replaceAll(it) }
            rv_product_sale_point.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv_product_sale_point.adapter = productsAdapter
            productsAdapter.listener = object : ClickableAdapter.BaseAdapterAction<ProductProvider> {
                override fun click(position: Int, data: ProductProvider, code: Int) {
                    context?.let {
                        if (data is IdentityData) {
                            val intent = Intent(context, ProductDetailActivity::class.java)
                            intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        if (dataProduct != null) {
            if (data.products != null) {
                if (data.products!!.data!!.isNotEmpty())
                    for (i in data.products!!.data?.indices!!) {
                        if (data.products!!.data!![i].id == dataProduct!!.id) {
                            productsAdapter.remove(data.products!!.data!![i])
                        }
                    }
            }

            linear_product_current.visibility = View.VISIBLE
            Glide.with(context).load(dataProduct!!.image)
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(img_product)

            tv_product.text = dataProduct!!.name
            tv_product_price.text = dataProduct!!.price.asMoney()
            tv_product_code.text = dataProduct!!.code

            constraintLayoutProduct.setOnClickListener {
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra(Const.TransferKey.EXTRA_ID, dataProduct!!.id)
                startActivity(intent)
            }

        } else linear_product_current.visibility = View.GONE
    }

    override
    fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(SalePointDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.getData.observe(this, Observer { p ->
            p.let {
                it?.let { it1 -> showDetail(it1) }
            }
        })
        viewModel.loadData(phone)
    }
}