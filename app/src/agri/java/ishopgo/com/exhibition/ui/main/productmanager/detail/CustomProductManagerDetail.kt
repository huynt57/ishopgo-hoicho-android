package ishopgo.com.exhibition.ui.main.productmanager.detail

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.LinearLayout
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.InfoProduct
import ishopgo.com.exhibition.model.product_manager.ProductManagerDetail
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.main.product.ProductAdapter
import ishopgo.com.exhibition.ui.widget.ItemOffsetDecoration
import ishopgo.com.exhibition.ui.widget.ProductDetailWidget
import kotlinx.android.synthetic.main.fragment_product_manager_detail.view.*

class CustomProductManagerDetail : ProductManagerDetailOverwrite() {
    private var product_Id: Long = 0L

    override fun handleOnCreate(productId: Long) {
        product_Id = productId
    }

    override fun handleViewCreated(rootView: View, context: Context) {
        rootView.apply {
            rv_product_related_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))
        }
    }

    companion object {
        const val NKSX_DISPLAY_SHOW: Int = 1 //Hiển thị NKSX
        const val NKSX_DISPLAY_HIDDEN: Int = 0 //Không hiển thị NKSX

        const val ACCREDITATINON_DISPLAY_SHOW: Int = 1 //Hiển thị bao tiêu
        const val ACCREDITATINON_DISPLAY_HIDDEN: Int = 0 //Không hiển thị bao tiêu
    }

    private var nkxs: Int = NKSX_DISPLAY_HIDDEN
    private var baoTieu: Int = ACCREDITATINON_DISPLAY_HIDDEN

    override fun handleStartEdit(rootView: View) {
        rootView.apply {
            edit_product_agri_scale.isFocusable = true
            edit_product_agri_scale.isFocusableInTouchMode = true
            edit_product_agri_quantity.isFocusable = true
            edit_product_agri_quantity.isFocusableInTouchMode = true
            edit_product_agri_expiryDate.isFocusable = true
            edit_product_agri_expiryDate.isFocusableInTouchMode = true
            edit_product_agri_pack.isFocusable = true
            edit_product_agri_pack.isFocusableInTouchMode = true
            edit_product_agri_season.isFocusable = true
            edit_product_agri_season.isFocusableInTouchMode = true
            edit_product_agri_shipmentCode.isFocusable = true
            edit_product_agri_shipmentCode.isFocusableInTouchMode = true
            edit_product_agri_manufacturingDate.isFocusable = true
            edit_product_agri_manufacturingDate.isFocusableInTouchMode = true
            edit_product_agri_harvestDate.isFocusable = true
            edit_product_agri_harvestDate.isFocusableInTouchMode = true
            edit_product_agri_shippedDate.isFocusable = true
            edit_product_agri_shippedDate.isFocusableInTouchMode = true
//            img_add_supplies_product.visibility = View.VISIBLE
//            img_add_solution_product.visibility = View.VISIBLE
            sw_show_accreditation.isClickable = true
            sw_show_nksx.isClickable = true
        }
    }

    override fun handleEndEdit(rootView: View) {
        rootView.apply {
            edit_product_agri_scale.isFocusable = false
            edit_product_agri_scale.isFocusableInTouchMode = false
            edit_product_agri_quantity.isFocusable = false
            edit_product_agri_quantity.isFocusableInTouchMode = false
            edit_product_agri_expiryDate.isFocusable = false
            edit_product_agri_expiryDate.isFocusableInTouchMode = false
            edit_product_agri_pack.isFocusable = false
            edit_product_agri_pack.isFocusableInTouchMode = false
            edit_product_agri_season.isFocusable = false
            edit_product_agri_season.isFocusableInTouchMode = false
            edit_product_agri_shipmentCode.isFocusable = false
            edit_product_agri_shipmentCode.isFocusableInTouchMode = false
            edit_product_agri_manufacturingDate.isFocusable = false
            edit_product_agri_manufacturingDate.isFocusableInTouchMode = false
            edit_product_agri_harvestDate.isFocusable = false
            edit_product_agri_harvestDate.isFocusableInTouchMode = false
            edit_product_agri_shippedDate.isFocusable = false
            edit_product_agri_shippedDate.isFocusableInTouchMode = false
//            img_add_supplies_product.visibility = View.GONE
//            img_add_solution_product.visibility = View.GONE
            sw_show_accreditation.isClickable = false
            sw_show_nksx.isClickable = false
        }
    }

    override fun handleInOtherFlavor(rootView: View, detail: ProductManagerDetail) {
        rootView.apply {
            val linearLayout = view_linear as LinearLayout
            linear_agri.visibility = View.VISIBLE
            linear_scale.visibility = View.GONE

            val convert = ProductManagerConverter().convert(detail)
            edit_product_agri_scale.setText(convert.providerScale())
            edit_product_agri_quantity.setText(convert.providerQuantity())
            edit_product_agri_expiryDate.setText(convert.providerHsd())
            edit_product_agri_pack.setText(convert.providerDongGoi())
            edit_product_agri_season.setText(convert.providerMuaVu())
            edit_product_agri_shipmentCode.setText(convert.providerMsLohang())
            edit_product_agri_manufacturingDate.setText(convert.providerNgaySx())
            edit_product_agri_harvestDate.setText(convert.providerDkThuhoach())
            edit_product_agri_shippedDate.setText(convert.providerXuatXuong())

            sw_show_nksx.isChecked = convert.providerIsNhatkySx()
            sw_show_nksx.text = if (convert.providerIsNhatkySx()) "Nhật ký sản xuất: Bật"
            else "Nhật ký sản xuất: Tắt"

            sw_show_accreditation.isChecked = convert.providerIsBaoTieu()
            sw_show_accreditation.text = if (convert.providerIsBaoTieu()) "Đã được bao tiêu: Đã được bao tiêu"
            else "Đã được bao tiêu: Chưa được bao tiêu"

            sw_show_nksx.setOnCheckedChangeListener { _, _ ->
                if (sw_show_nksx.isChecked) {
                    nkxs = NKSX_DISPLAY_SHOW
                    sw_show_nksx.text = "Nhật ký sản xuất: Bật"
                } else {
                    nkxs = NKSX_DISPLAY_HIDDEN
                    sw_show_nksx.text = "Nhật ký sản xuất: Tắt"
                }
            }

            sw_show_accreditation.setOnCheckedChangeListener { _, _ ->
                if (sw_show_accreditation.isChecked) {
                    baoTieu = ACCREDITATINON_DISPLAY_SHOW
                    sw_show_accreditation.text = "Đã được bao tiêu: Đã được bao tiêu"
                } else {
                    baoTieu = ACCREDITATINON_DISPLAY_HIDDEN
                    sw_show_accreditation.text = "Đã được bao tiêu: Chưa được bao tiêu"
                }
            }


            if (convert.providerInfo().isNotEmpty()) {
                val listInfo = convert.providerInfo()
                for (i in listInfo.indices) {
                    val productInfo = ProductDetailWidget(rootView.context)
                    productInfo.apply {
                        label_products.text = listInfo[i].name
                        val adapter = ProductAdapter(0.4f)
                        if (listInfo[i].products?.data?.isNotEmpty() == true) {
                            listInfo[i].products?.data?.let { adapter.replaceAll(it) }

                            rv_product_related_products.adapter = adapter
                            val layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                            layoutManager.isAutoMeasureEnabled = true
                            rv_product_related_products.layoutManager = layoutManager
                            rv_product_related_products.isNestedScrollingEnabled = false
                            rv_product_related_products.addItemDecoration(ItemOffsetDecoration(context, R.dimen.item_spacing))

                            linearLayout.addView(productInfo)
//                            adapter.listener = object : ClickableAdapter.BaseAdapterAction<Product> {
//                                override fun click(position: Int, data: Product, code: Int) {
//                                    context?.let {
//                                        val intent = Intent(context, ProductDetailActivity::class.java)
//                                        intent.putExtra(Const.TransferKey.EXTRA_ID, data.id)
//                                        it.startActivity(intent)
//                                    }
//                                }
//                            }
                        }
                    }
                }

            }
        }
    }

    interface ProductManagerDetailProvider {
        fun providerScale(): String
        fun providerQuantity(): String
        fun providerIsNhatkySx(): Boolean
        fun providerIsBaoTieu(): Boolean
        fun providerNgaySx(): String
        fun providerDkThuhoach(): String
        fun providerMsLohang(): String
        fun providerMuaVu(): String
        fun providerDongGoi(): String
        fun providerHsd(): String
        fun providerXuatXuong(): String
        fun providerInfo(): List<InfoProduct>
    }

    class ProductManagerConverter : Converter<ProductManagerDetail, ProductManagerDetailProvider> {

        override fun convert(from: ProductManagerDetail): ProductManagerDetailProvider {
            return object : ProductManagerDetailProvider {
                override fun providerIsNhatkySx(): Boolean {
                    return from.isNhatkySx == 1
                }

                override fun providerIsBaoTieu(): Boolean {
                    return from.isBaoTieu == 1
                }

                override fun providerNgaySx(): String {
                    return from.ngaySx ?: ""
                }

                override fun providerDkThuhoach(): String {
                    return from.dkThuhoach ?: ""
                }

                override fun providerMsLohang(): String {
                    return from.msLohang ?: ""
                }

                override fun providerMuaVu(): String {
                    return from.muaVu ?: ""
                }

                override fun providerDongGoi(): String {
                    return from.dongGoi ?: ""
                }

                override fun providerHsd(): String {
                    return from.hsd ?: ""
                }

                override fun providerXuatXuong(): String {
                    return from.xuatXuong ?: ""
                }

                override fun providerScale(): String {
                    return from.quyMo ?: ""
                }

                override fun providerQuantity(): String {
                    return from.sanLuong ?: ""
                }

                override fun providerInfo(): List<InfoProduct> {
                    return from.info ?: mutableListOf()
                }
            }
        }
    }
}