package ishopgo.com.exhibition.ui.main.product.icheckproduct.update

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IcheckProduct
import ishopgo.com.exhibition.domain.response.IcheckVendor
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseActionBarFragment
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.Toolbox
import kotlinx.android.synthetic.main.content_icheck_update_product.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class IcheckUpdateProductFragment : BaseActionBarFragment() {
    companion object {

        fun newInstance(params: Bundle): IcheckUpdateProductFragment {
            val fragment = IcheckUpdateProductFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private var data: IcheckProduct? = null
    private var image = ""
    private var code = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        code = arguments?.getString(Const.TransferKey.EXTRA_REQUIRE) ?: ""

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        data = Toolbox.gson.fromJson(json, IcheckProduct::class.java)
    }

    override fun contentLayoutRes(): Int {
        return R.layout.content_icheck_update_product
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbars()

        view_image_add_product.setOnClickListener { launchPickPhotoIntent() }

        if (data != null) {
            val convert = ProductDetailConverter().convert(data!!)

            Glide.with(context)
                    .load(convert.provideProductImage())
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder))
                    .into(view_image_add_product)

            edit_product_tenSp.setText(convert.provideProductName())
            edit_product_maSp.setText(convert.provideProductBarCode())
            edit_product_giaBan.setText(convert.provideProductPrice())

            if (data!!.attributes?.isNotEmpty() == true) {
                val attribute = data!!.attributes!![0]
                edit_product_moTa.setText(attribute.shortContent)
            }

            val vendor = convert.provideProductVendor()
            if (vendor != null) {
                edit_product_tenDoanhNghiep.setText(vendor.name)
                edit_product_soDienThoai.setText(vendor.phone)
                edit_product_email.setText(vendor.email)
                edit_product_website.setText(vendor.website)
                edit_product_diaChi.setText(vendor.address)

                if (vendor.country != null) {
                    edit_product_quocGia.setText(vendor.country?.name ?: "")
                }
            }
        }

        if (code.isNotBlank()){
            edit_product_maSp.setText(code)
        }
    }

    private fun launchPickPhotoIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Const.RequestCode.RC_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Const.RequestCode.RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            if (Toolbox.exceedSize(context!!, data.data, (5 * 1024 * 1024).toLong())) {
                toast("Chỉ đính kèm được ảnh có dung lượng dưới 5 MB. Hãy chọn file khác.")
                return
            }

            image = data.data.toString()

            Glide.with(context)
                    .load(Uri.parse(image))
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder))
                    .into(view_image_add_product)
        }

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
                    return "http://ucontent.icheck.vn/" + from.imageDefault + "_medium.jpg"
                }

                override fun provideProductName(): CharSequence {
                    return from.productName ?: ""
                }

                override fun provideProductPrice(): CharSequence {
                    return from.priceDefault.toString()

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
        toolbar.setCustomTitle("Bổ sung thông tin sản phẩm")
        toolbar.leftButton(R.drawable.ic_arrow_back_highlight_24dp)
        toolbar.setLeftButtonClickListener {
            activity?.finish()
        }
    }
}