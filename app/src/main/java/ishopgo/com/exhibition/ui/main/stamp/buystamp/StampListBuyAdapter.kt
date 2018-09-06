package ishopgo.com.exhibition.ui.main.stamp.buystamp

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.StampListBuy
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asMoney
import kotlinx.android.synthetic.main.item_stamp_orders.view.*

class StampListBuyAdapter : ClickableAdapter<StampListBuy>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_stamp_orders
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<StampListBuy> {
        return Holder(v, StampManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<StampListBuy>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<StampListBuy, StampManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<StampListBuy>(v) {

        override fun populate(data: StampListBuy) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_maDonHang.text = convert.provideCode()
                tv_ngayMua.text = convert.provideDate()
                tv_nguoiDat.text = convert.provideUserName()
                tv_soLuong.text = convert.provideQuantity()
                tv_donGia.text = convert.provideUnitPrice()
                tv_thanhTien.text = convert.providePrice()
                tv_tv_ghiChu.text = convert.provideNote()
            }
        }
    }

    interface StampManagerProvider {
        fun provideCode(): CharSequence
        fun provideDate(): CharSequence
        fun provideUserName(): CharSequence
        fun provideQuantity(): CharSequence
        fun provideUnitPrice(): CharSequence
        fun providePrice(): CharSequence
        fun provideNote(): CharSequence
    }

    class StampManagerConverter : Converter<StampListBuy, StampManagerProvider> {
        override fun convert(from: StampListBuy): StampManagerProvider {
            return object : StampManagerProvider {
                override fun provideCode(): CharSequence {
                    return "Mã đơn hàng: ${from.code ?: ""}"
                }

                override fun provideDate(): CharSequence {
                    return from.createdAt?.asDateTime() ?: ""
                }

                override fun provideUserName(): CharSequence {
                    return "Người đăng: ${from.userName ?: ""}"
                }

                override fun provideQuantity(): CharSequence {
                    return "Số lượng: ${from.quantity ?: ""}"
                }

                override fun provideUnitPrice(): CharSequence {
                    return "Đơn giá: ${from.unitPrice?.asMoney() ?: 0L}"
                }
                override fun providePrice(): CharSequence {
                    return "Thành tiền: ${from.priceTotal?.asMoney() ?: 0L}"
                }
                override fun provideNote(): CharSequence {
                    return "Ghi chú: ${from.note ?: ""}"
                }
            }
        }
    }
}