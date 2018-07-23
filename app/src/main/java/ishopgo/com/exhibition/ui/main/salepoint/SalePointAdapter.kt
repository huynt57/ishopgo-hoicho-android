package ishopgo.com.exhibition.ui.main.salepoint

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.SalePoint
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_list_sale_point.view.*

class SalePointAdapter : ClickableAdapter<SalePoint>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_list_sale_point
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<SalePoint> {
        return Holder(v, MemberConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<SalePoint>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.btn_status.setOnClickListener {
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }
    }

    inner class Holder(v: View, private val converter: Converter<SalePoint, SalePointProvider>) : BaseRecyclerViewAdapter.ViewHolder<SalePoint>(v) {

        override fun populate(data: SalePoint) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_sale_point_product_name.text = convert.provideProductName()
                tv_sale_point_product_phone.setPhone(convert.providePhone(), data.phone ?: "")
                tv_sale_point_name.text = convert.provideName()
                tv_sale_point_address.text = convert.provideAddress()
                tv_sale_point_district.text = convert.provideDistrict()
                tv_sale_point_city.text = convert.provideCity()
                btn_status.isChecked = convert.provideIsShowing()
                btn_status.text = convert.provideStatus()
            }
        }
    }

    interface SalePointProvider {
        fun provideName(): String
        fun providePhone(): CharSequence
        fun provideCity(): String
        fun provideAddress(): String
        fun provideDistrict(): String
        fun providePrice(): String
        fun provideStatus(): String
        fun provideIsShowing(): Boolean
        fun provideProductName(): String
    }

    class MemberConverter : Converter<SalePoint, SalePointProvider> {

        override fun convert(from: SalePoint): SalePointProvider {
            return object : SalePointProvider {

                override fun provideIsShowing(): Boolean {
                    return isShowing()
                }

                override fun provideCity(): String {
                    return "Thành phố: ${from.city}"
                }

                override fun provideDistrict(): String {
                    return "Quận huyện: ${from.district}"
                }

                override fun providePrice(): String {
                    return if (from.price == 0L) "Liên hệ"
                    else return from.price?.asMoney() ?: "0 đ"
                }

                override fun provideStatus(): String {
                    return if (isShowing()) "Hiển thị" else "Ẩn"
                }

                override fun provideProductName(): String {
                    return "Sản phẩm: ${from.productName ?: ""}"
                }

                override fun provideName(): String {
                    return "Tên điểm bán: ${from.name}"
                }

                override fun providePhone(): CharSequence {
                    return from.phone ?: ""
                }

                override fun provideAddress(): String {
                    return "Địa chỉ: ${from.address}"
                }

                private fun isShowing() = from.status == STATUS_SHOW
            }
        }

    }

    companion object {
        val STATUS_SHOW = 1
    }
}