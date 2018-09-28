package ishopgo.com.exhibition.ui.main.stamp.stampwarning

import android.annotation.SuppressLint
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.StampListWarning
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_stamp_warning.view.*
import net.glxn.qrgen.android.QRCode

class StampWarningAdapter : ClickableAdapter<StampListWarning>() {
    companion object {
        const val CLICK_BUTTOM_THU_HOI = 0
        const val CLICK_ITEMVIEW = 1
    }

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_stamp_warning
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<StampListWarning> {
        return Holder(v, StampManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<StampListWarning>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.btn_thuHoi.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_BUTTOM_THU_HOI) }
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_ITEMVIEW) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<StampListWarning, StampManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<StampListWarning>(v) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: StampListWarning) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                img_qrCode.setImageBitmap(QRCode.from("http://${resources.getString(R.string.app_host)}/check/${convert.provideCode()}").withSize(300, 300).bitmap())

                tv_qrCode.text = convert.provideCode()
                tv_dateCreate.text = convert.provideDate()
                tv_productName.text = convert.provideProductName()
                tv_countScan.text = convert.provideCountScan()
                tv_countProple.text = convert.provideCountPeople()
                if (convert.provideStatusWarning().isNotEmpty()) btn_thuHoi.text = convert.provideStatusWarning()
                else btn_thuHoi.text = "Thu hồi"
            }
        }
    }

    interface StampManagerProvider {
        fun provideCode(): CharSequence
        fun provideDate(): CharSequence
        fun provideProductName(): CharSequence
        fun provideCountScan(): CharSequence
        fun provideCountPeople(): CharSequence
        fun provideStatusWarning(): CharSequence
    }

    class StampManagerConverter : Converter<StampListWarning, StampManagerProvider> {
        override fun convert(from: StampListWarning): StampManagerProvider {
            return object : StampManagerProvider {
                override fun provideStatusWarning(): CharSequence {
                    return from.statusWarning ?: ""
                }

                override fun provideCode(): CharSequence {
                    return "Mã tem: ${from.code ?: ""}"
                }

                override fun provideDate(): CharSequence {
                    return from.createdAt ?: ""
                }

                override fun provideProductName(): CharSequence {
                    return "Tên sản phẩm: ${from.productName ?: ""}"
                }

                override fun provideCountScan(): CharSequence {
                    return "Số lượt quét: ${from.numberOfScans ?: 0}"
                }

                override fun provideCountPeople(): CharSequence {
                    return "Số người quét: ${from.numberOfUsers ?: 0}"
                }
            }
        }
    }
}