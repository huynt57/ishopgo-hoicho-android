package ishopgo.com.exhibition.ui.main.stamp.buystamp.update.history

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.StampOrderHistory
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import kotlinx.android.synthetic.main.item_stamp_order_history.view.*

class BuyStampHistoryAdapter : ClickableAdapter<StampOrderHistory>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_stamp_order_history
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<StampOrderHistory> {
        return Holder(v, StampManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<StampOrderHistory>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<StampOrderHistory, StampManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<StampOrderHistory>(v) {

        override fun populate(data: StampOrderHistory) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_hanhDong.text = convert.provideStatus()
                tv_ngaySua.text = convert.provideDate()
                tv_nhanVien.text = convert.provideUserName()
                if (data.statusName?.isNotEmpty() == true) {
                    tv_ghiChu.text = convert.provideNote()
                    tv_ghiChu.visibility = View.VISIBLE
                }
            }
        }
    }

    interface StampManagerProvider {
        fun provideDate(): CharSequence
        fun provideUserName(): CharSequence
        fun provideNote(): CharSequence
        fun provideStatus(): CharSequence
    }

    class StampManagerConverter : Converter<StampOrderHistory, StampManagerProvider> {
        override fun convert(from: StampOrderHistory): StampManagerProvider {
            return object : StampManagerProvider {
                override fun provideStatus(): CharSequence {
                    return "Hành động: ${from.statusName ?: ""}"
                }

                override fun provideDate(): CharSequence {
                    return from.createdAt?.asDateTime() ?: ""
                }

                override fun provideUserName(): CharSequence {
                    return "Nhân viên: ${from.userName ?: ""}"
                }

                override fun provideNote(): CharSequence {
                    return "Ghi chú: ${from.note ?: ""}"
                }
            }
        }
    }
}