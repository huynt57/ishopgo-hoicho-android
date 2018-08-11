package ishopgo.com.exhibition.ui.main.product.icheckproduct

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IcheckSalePoint
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asMoney
import kotlinx.android.synthetic.main.item_product_sale_point.view.*

class IcheckSalePointAdapter : ClickableAdapter<IcheckSalePoint>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_sale_point
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<IcheckSalePoint> {
        return Holder(v, MemberConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<IcheckSalePoint>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<IcheckSalePoint, SalePointProvider>) : BaseRecyclerViewAdapter.ViewHolder<IcheckSalePoint>(v) {

        override fun populate(data: IcheckSalePoint) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_product_sale_point_name.text = convert.provideName()
                tv_product_sale_point_address.text = convert.provideAddress()
                tv_product_sale_point_distance.text = convert.provideDistance()
                tv_product_sale_point_distance.visibility = View.VISIBLE
                tv_product_sale_point_phone.visibility = View.GONE
                tv_product_sale_point_price.text = convert.providePrice()
            }
        }
    }

    interface SalePointProvider {
        fun provideName(): CharSequence
        fun provideAddress(): CharSequence
        fun provideDistance(): CharSequence
        fun providePrice(): CharSequence
    }

    class MemberConverter : Converter<IcheckSalePoint, SalePointProvider> {

        override fun convert(from: IcheckSalePoint): SalePointProvider {
            return object : SalePointProvider {
                override fun providePrice(): CharSequence {
                    return from.price?.asMoney() ?: "0 đ"
                }

                override fun provideDistance(): CharSequence {
                    return "${from.distance ?: 0.0F}km"
                }

                override fun provideName(): CharSequence {
                    return "Tên điểm bán: <b>${from.name}</b>".asHtml()
                }

                override fun provideAddress(): String {
                    return "Địa chỉ: ${from.address}, ${from.district?.name
                            ?: ""}, ${from.city?.name ?: ""}"
                }
            }
        }

    }
}