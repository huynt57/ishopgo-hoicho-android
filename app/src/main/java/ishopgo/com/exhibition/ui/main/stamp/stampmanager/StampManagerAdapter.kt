package ishopgo.com.exhibition.ui.main.stamp.stampmanager

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.StampManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_stamp_manager.view.*
import net.glxn.qrgen.android.QRCode

class StampManagerAdapter : ClickableAdapter<StampManager>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_stamp_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<StampManager> {
        return Holder(v, StampManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<StampManager>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<StampManager, StampManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<StampManager>(v) {

        override fun populate(data: StampManager) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                img_qrCode.setImageBitmap(QRCode.from(convert.provideCode().toString()).withSize(300, 300).bitmap())

                tv_qrCode.text = convert.provideCode()
                tv_dateCreate.text = convert.provideDate()
                tv_productName.text = convert.provideProductName()
                tv_countScan.text = convert.provideCountScan()
                tv_countProple.text = convert.provideCountPeople()
            }
        }
    }

    interface StampManagerProvider {
        fun provideCode(): CharSequence
        fun provideDate(): CharSequence
        fun provideProductName(): CharSequence
        fun provideCountScan(): CharSequence
        fun provideCountPeople(): CharSequence
    }

    class StampManagerConverter : Converter<StampManager, StampManagerProvider> {
        override fun convert(from: StampManager): StampManagerProvider {
            return object : StampManagerProvider {
                override fun provideCode(): CharSequence {
                    return "Mã tem: ${from.code ?: ""}"
                }

                override fun provideDate(): CharSequence {
                    return from.createAt ?: ""
                }

                override fun provideProductName(): CharSequence {
                    return "Tên sản phẩm: ${from.productName ?: ""}"
                }

                override fun provideCountScan(): CharSequence {
                    return "Số lượt quét: ${from.scanTotal ?: 0}"
                }

                override fun provideCountPeople(): CharSequence {
                    return "Số người quét: ${from.scanTotal ?: 0}"
                }

            }
        }
    }
}