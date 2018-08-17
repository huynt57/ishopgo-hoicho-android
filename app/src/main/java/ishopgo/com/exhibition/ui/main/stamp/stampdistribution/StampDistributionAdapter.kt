package ishopgo.com.exhibition.ui.main.stamp.stampdistribution

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.StampDistribution
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_stamp_distribution.view.*

class StampDistributionAdapter : ClickableAdapter<StampDistribution>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_stamp_distribution
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<StampDistribution> {
        return Holder(v, StampManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<StampDistribution>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.btn_saveQrCode.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<StampDistribution, StampManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<StampDistribution>(v) {

        override fun populate(data: StampDistribution) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_qrCodeStart.text = convert.provideStampStart()
                tv_qrCodeEnd.text = convert.provideStampEnd()
                tv_userName.text = convert.provideUser()
                tv_productName.text = convert.provideProductName()
                tv_stampWarning.text = convert.provideStampWarning()
            }
        }
    }

    interface StampManagerProvider {
        fun provideStampStart(): CharSequence
        fun provideStampEnd(): CharSequence
        fun provideProductName(): CharSequence
        fun provideStampWarning(): CharSequence
        fun provideUser(): CharSequence
    }

    class StampManagerConverter : Converter<StampDistribution, StampManagerProvider> {
        override fun convert(from: StampDistribution): StampManagerProvider {
            return object : StampManagerProvider {
                override fun provideStampStart(): CharSequence {
                    return "Tem bắt đầu: ${from.stampSerialNumberPrefix ?: ""} - ${from.startQuantity ?: ""}"
                }

                override fun provideStampEnd(): CharSequence {
                    return "Tem kết thúc: ${from.stampSerialNumberPrefix ?: ""} - ${from.endQuantity ?: ""}"
                }

                override fun provideProductName(): CharSequence {
                    return "Tên sản phẩm: ${from.productName ?: ""}"
                }

                override fun provideStampWarning(): CharSequence {
                    return "Giới hạn cảnh báo: ${from.limitedAccess ?: ""}"
                }

                override fun provideUser(): CharSequence {
                    return "Người tạo: ${from.accountName ?: ""}"
                }
            }
        }
    }
}