package ishopgo.com.exhibition.ui.main.product.detail.add_sale_point

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ProductDetail
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.District
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.login.RegionAdapter
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailViewModel
import ishopgo.com.exhibition.ui.main.salepoint.DistrictAdapter
import ishopgo.com.exhibition.ui.widget.Toolbox
import kotlinx.android.synthetic.main.fragment_product_add_sale_point.*

class ProductSalePointAddFragment : BaseFragment() {
    private lateinit var viewModel: ProductDetailViewModel
    private val adapterRegion = RegionAdapter()
    private val adapterDistrict = DistrictAdapter()
    private lateinit var data: ProductDetail

    companion object {
        fun newInstance(params: Bundle): ProductSalePointAddFragment {
            val fragment = ProductSalePointAddFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_add_sale_point, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.getDefaultGson().fromJson(json, ProductDetail::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(context).load(data.image)
                .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(img_product)

        tv_product.text = data.name
        tv_product_price.text = data.price.asMoney()

        edit_shop_city.setOnClickListener { getRegion(edit_shop_city) }
        edit_shop_district.setOnClickListener { getDistrict(edit_shop_district) }

        btn_sale_point_add.setOnClickListener {
            if (checkRequireFields(edit_product_price.text.toString(), edit_shop_phone.text.toString(), edit_shop_name.text.trim().toString(), edit_shop_city.text.toString(), edit_shop_district.text.toString()))
                viewModel.createProductSalePoint(data.id, edit_product_price.text.toString(), edit_shop_phone.text.toString(), edit_shop_name.text.toString(), edit_shop_city.text.toString(),
                        edit_shop_district.text.toString(), edit_shop_address.text.toString())
        }
    }

    private fun checkRequireFields(price: String, phone: String, name: String, city: String, district: String): Boolean {

        if (price.trim().isEmpty()) {
            toast("Giá sản phẩm không được để trống")
            edit_product_price.error = getString(R.string.error_field_required)
            edit_product_price.requestFocus()
            val inputMethodManager = edit_product_price.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(edit_product_price, 0)
            return false
        }

        if (phone.trim().isEmpty()) {
            toast("Số điện thoại không được để trống")
            edit_shop_phone.error = getString(R.string.error_field_required)
            edit_shop_phone.requestFocus()
            val inputMethodManager = edit_shop_phone.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(edit_shop_phone, 0)
            return false
        }

        if (name.trim().isEmpty()) {
            toast("Tên cửa hàng không được để trống")
            edit_shop_name.error = getString(R.string.error_field_required)
            edit_shop_name.requestFocus()
            val inputMethodManager = edit_shop_name.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(edit_shop_name, 0)
            return false
        }

        if (city.trim().isEmpty()) {
            toast("Bạn chưa chọn thành phố")
            edit_shop_city.error = getString(R.string.error_field_required)
            edit_shop_city.requestFocus()
            return false
        }

        if (district.trim().isEmpty()) {
            toast("Bạn chưa chọn quận huyện")
            edit_shop_district.error = getString(R.string.error_field_required)
            edit_shop_district.requestFocus()
            return false
        }

        return true
    }

    private fun getRegion(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn thành phố")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            rv_search.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)

            rv_search.adapter = adapterRegion
            adapterRegion.listener = object : ClickableAdapter.BaseAdapterAction<Region> {
                override fun click(position: Int, data: Region, code: Int) {
                    context?.let {
                        data.provinceid?.let { it1 -> viewModel.loadDistrict(it1) }
                        dialog.dismiss()
                        view.text = data.name
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    private fun getDistrict(view: TextView) {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .title("Chọn quận huyện")
                    .customView(R.layout.diglog_search_recyclerview, false)
                    .negativeText("Huỷ")
                    .onNegative { dialog, _ -> dialog.dismiss() }
                    .autoDismiss(false)
                    .canceledOnTouchOutside(false)
                    .build()

            val rv_search = dialog.findViewById(R.id.rv_search) as RecyclerView
            val edt_search = dialog.findViewById(R.id.textInputLayout) as TextInputLayout
            edt_search.visibility = View.GONE

            rv_search.layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)

            rv_search.adapter = adapterDistrict
            adapterDistrict.listener = object : ClickableAdapter.BaseAdapterAction<District> {
                override fun click(position: Int, data: District, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        view.text = data.name
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    override
    fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(ProductDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.createSalePointSuccess.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                activity?.setResult(Activity.RESULT_OK)
                toast("Thêm điểm bán thành công")
                activity?.finish()
            }
        })

        viewModel.loadRegion.observe(this, Observer { p ->
            p?.let {
                adapterRegion.replaceAll(it)
            }
        })

        viewModel.loadDistrict.observe(this, Observer { p ->
            p?.let {
                adapterDistrict.replaceAll(it)
            }
        })

        viewModel.loadRegion()
    }
}