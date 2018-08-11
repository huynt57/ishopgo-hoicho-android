package ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint

import android.os.Bundle
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
import ishopgo.com.exhibition.ui.extensions.asMoney
import kotlinx.android.synthetic.main.content_icheck_sale_point_add.*
import kotlinx.android.synthetic.main.fragment_base_actionbar.*

class IcheckSalePointAddFragment : BaseActionBarFragment() {
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

        if (data!=null){
            val convert = ProductDetailConverter().convert(data!!)

            Glide.with(context).load(convert.provideProductImage())
                    .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(img_product)
            tv_product.text = convert.provideProductName()
            tv_product_price.text = convert.provideProductPrice()
            tv_product_code.text = convert.provideProductBarCode()
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