package ishopgo.com.exhibition.ui.main.product.detail

import android.text.Spanned
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.ProductSalePoint
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.extensions.asPhone
import kotlinx.android.synthetic.main.item_product_sale_point.view.*

class ProductSalePointAdapter : ClickableAdapter<ProductSalePoint>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_sale_point
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ProductSalePoint> {
        return Holder(v, ProductSalePointConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<ProductSalePoint>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<ProductSalePoint, ProductSalePointProvider>) : BaseRecyclerViewAdapter.ViewHolder<ProductSalePoint>(v) {

        override fun populate(data: ProductSalePoint) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_product_sale_point_address.text = convert.provideAddress()
                tv_product_sale_point_name.text = convert.provideName()
                tv_product_sale_point_phone.text = convert.providePhone()
                tv_product_sale_point_price.text = convert.providePrice()
            }
        }
    }

    interface ProductSalePointProvider {
        fun provideName(): Spanned
        fun provideAddress(): String
        fun providePrice(): String
        fun providePhone(): String
    }

    class ProductSalePointConverter : Converter<ProductSalePoint, ProductSalePointProvider> {

        override fun convert(from: ProductSalePoint): ProductSalePointProvider {
            return object : ProductSalePointProvider {
                override fun provideAddress(): String {
                    return "${from.address ?: ""}, ${from.district ?: ""}, ${from.city ?: ""}"
                }

                override fun providePrice(): String {
                    return from.price?.asMoney() ?: "0 đ"
                }

                override fun providePhone(): String {
                    return from.phone?.asPhone() ?: ""
                }

                override fun provideName(): Spanned {
                    return "<b>${from.name ?: ""}</b>".asHtml()
                }

            }
        }
    }
}