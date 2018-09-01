package ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IcheckCity
import ishopgo.com.exhibition.domain.response.IcheckDistrict
import ishopgo.com.exhibition.domain.response.IcheckProduct
import ishopgo.com.exhibition.domain.response.IcheckVendor
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.District
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckCityAdapter
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckDistrictAdapter
import ishopgo.com.exhibition.ui.main.product.icheckproduct.IcheckProductViewModel
import kotlinx.android.synthetic.main.content_icheck_sale_point_add.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*
import kotlinx.android.synthetic.main.fragment_signup.*

class IcheckSalePointAddFragment : BaseActionBarFragment() {
    private lateinit var viewModel: IcheckProductViewModel
    private var cityId = -1L
    private var districtId = -1L
    private var categoryId = -1L
    private var lat = 0.0F
    private var long = 0.0F
    private val adapterRegion = IcheckCityAdapter()
    private val adapterDistrict = IcheckDistrictAdapter()

    companion object {

        fun newInstance(params: Bundle): IcheckSalePointAddFragment {
            val fragment = IcheckSalePointAddFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private var data: IcheckProduct? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, IcheckProduct::class.java)
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_icheck_sale_point_add
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        if (data != null) {
            val convert = ProductDetailConverter().convert(data!!)

            Glide.with(context).load(convert.provideProductImage())
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(img_product)
            tv_product.text = convert.provideProductName()
            tv_product_price.text = convert.provideProductPrice()
            tv_product_code.text = convert.provideProductBarCode()

            btn_sale_point_add.setOnClickListener {
                viewModel.createIcheckSalePoint(data?.code
                        ?: "", edit_shop_diemBan.text.toString(), edit_product_giaBan.money
                        ?: -1L, edit_shop_sdt.text.toString(), cityId, districtId, edit_shop_diaChi.text.toString(), lat, long, edit_shop_nguoiGT.text.toString(), categoryId)
            }
        } else {
            btn_sale_point_add.setOnClickListener {
                toast("Có lỗi xảy ra, không tìm thấy sản phẩm")
            }
        }

        edit_shop_thanhPho.setOnClickListener {
            getRegion(edit_shop_thanhPho)
        }

        edit_shop_district.setOnClickListener {
            getDistrict(edit_shop_district)
        }

        edt_product_danhMuc.setOnClickListener {
            viewModel.openIcheckCategoryFragment()
        }
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
            adapterRegion.listener = object : ClickableAdapter.BaseAdapterAction<IcheckCity.City> {
                override fun click(position: Int, data: IcheckCity.City, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        val requestDistrict = String.format("https://api-affiliate.icheck.com.vn:6086/districts?city_id=%s", data.id)
                        viewModel.loadIcheckShopDistrict(requestDistrict)
                        cityId = data.id
                        view.text = data.cityName
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
            adapterDistrict.listener = object : ClickableAdapter.BaseAdapterAction<IcheckDistrict.District> {
                override fun click(position: Int, data: IcheckDistrict.District, code: Int) {
                    context?.let {
                        dialog.dismiss()
                        districtId = data.id
                        view.text = data.districtName
                        view.error = null
                    }
                }
            }
            dialog.show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(IcheckProductViewModel::class.java, true)
        viewModel.errorSignal.observe(this, Observer { baseErrorSignal ->
            baseErrorSignal?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.createSalePointSucccess.observe(this, Observer {
            toast("Tạo thành công")
            activity?.setResult(RESULT_OK)
            activity?.finish()
        })
        viewModel.dataCity.observe(this, Observer { p ->
            p?.let {
                it.cities?.let { it1 -> adapterRegion.replaceAll(it1) }
            }
        })
        viewModel.dataDistrict.observe(this, Observer { p ->
            p?.let {
                it.districts?.let { it1 -> adapterDistrict.replaceAll(it1) }
            }
        })

        viewModel.dataResultCategoryId.observe(this, Observer { p ->
            p?.let {
                categoryId = it
            }
        })

        viewModel.dataResultCategoryName.observe(this, Observer { p ->
            p?.let {
                edt_product_danhMuc.setText(it ?: "")
            }
        })

        viewModel.loadIcheckShopCity("https://api-affiliate.icheck.com.vn:6086/cities")
    }

    interface ProductDetailProvider {

        fun provideProductImage(): String
        fun provideProductName(): CharSequence
        fun provideProductPrice(): CharSequence
        fun provideProductBarCode(): CharSequence
        fun provideProductVendor(): IcheckVendor?
    }

    class ProductDetailConverter : Converter<IcheckProduct, ProductDetailProvider> {

        override fun convert(from: IcheckProduct): ProductDetailProvider {
            return object : ProductDetailProvider {

                override fun provideProductImage(): String {
                    val linkImage = from.imageDefault ?: ""
                    return if (linkImage.toLowerCase().startsWith("http")) linkImage else "http://ucontent.icheck.vn/" + linkImage + "_medium.jpg"
                }

                override fun provideProductName(): CharSequence {
                    return from.productName ?: ""
                }

                override fun provideProductPrice(): CharSequence {
                    return from.priceDefault.asMoney()

                }

                override fun provideProductBarCode(): CharSequence {
                    return from.code ?: ""
                }

                override fun provideProductVendor(): IcheckVendor? {
                    return from.vendor
                }

            }
        }
    }

    private fun setupToolbars() {
        toolbar.setCustomTitle("Thêm điểm bán")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }
}