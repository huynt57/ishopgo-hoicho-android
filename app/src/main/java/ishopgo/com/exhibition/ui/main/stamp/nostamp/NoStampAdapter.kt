package ishopgo.com.exhibition.ui.main.stamp.nostamp

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.StampNoList
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_no_stamp.view.*

class NoStampAdapter : ClickableAdapter<StampNoList>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_no_stamp
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<StampNoList> {
        return Holder(v, StampManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<StampNoList>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<StampNoList, StampManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<StampNoList>(v) {

        override fun populate(data: StampNoList) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_qrCode.text = convert.provideCode()
                tv_userName.text = convert.provideUser()
                tv_stampName.text = convert.provideStampName()
                tv_subDivision.text = convert.provideSubDivision()
                tv_countStanp.text = convert.provideCountStamp()
                tv_countRest.text = convert.provideCountRest()
            }
        }
    }

    interface StampManagerProvider {
        fun provideCode(): CharSequence
        fun provideUser(): CharSequence
        fun provideSubDivision(): CharSequence
        fun provideStampName(): CharSequence
        fun provideCountStamp(): CharSequence
        fun provideCountRest(): CharSequence
    }

    class StampManagerConverter : Converter<StampNoList, StampManagerProvider> {
        override fun convert(from: StampNoList): StampManagerProvider {
            return object : StampManagerProvider {
                override fun provideUser(): CharSequence {
                    return from.userName ?: ""
                }

                override fun provideSubDivision(): CharSequence {
                    return "Phân lô: ${from.assignProduct ?: "0"}"
                }

                override fun provideStampName(): CharSequence {
                    return "Tên lô: ${from.name ?: ""}"
                }

                override fun provideCountStamp(): CharSequence {
                    return "Số lượng: ${from.quantity ?: ""}"
                }

                override fun provideCountRest(): CharSequence {
                    return "Số lượng còn: ${from.quantityExists ?: ""}"
                }

                override fun provideCode(): CharSequence {
                    return "Mã lô: ${from.serialNumberPrefix ?: ""}"
                }
            }
        }
    }
}