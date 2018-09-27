package ishopgo.com.exhibition.ui.main.stamp.nostamp

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.StampNoList
import ishopgo.com.exhibition.domain.response.StampNoListNew
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_no_stamp.view.*

class NoStampAdapter : ClickableAdapter<StampNoListNew>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_no_stamp
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<StampNoListNew> {
        return Holder(v, StampManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<StampNoListNew>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<StampNoListNew, StampManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<StampNoListNew>(v) {

        override fun populate(data: StampNoListNew) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_qrCode.text = convert.provideCode()
                tv_userName.text = convert.provideUser()
                tv_productName.text = convert.provideProductName()
                tv_download.text = convert.provideDownload()
                tv_limitAccess.text = convert.provideLimitAccess()
                tv_note.visibility = if (data.note?.isNotEmpty() == true) View.VISIBLE else View.GONE
                tv_note.text = convert.provideNote()
            }
        }
    }

    interface StampManagerProvider {
        fun provideCode(): CharSequence
        fun provideUser(): CharSequence
        fun provideNote(): CharSequence
        fun provideLimitAccess(): CharSequence
        fun provideDownload(): CharSequence
        fun provideProductName(): CharSequence
    }

    class StampManagerConverter : Converter<StampNoListNew, StampManagerProvider> {
        override fun convert(from: StampNoListNew): StampManagerProvider {
            return object : StampManagerProvider {
                override fun provideUser(): CharSequence {
                    return from.accountName ?: ""
                }

                override fun provideNote(): CharSequence {
                    return "Ghi chú: ${from.note ?: ""}"
                }

                override fun provideLimitAccess(): CharSequence {
                    return "Hạn cảnh báo: ${from.limitedAccess ?: ""}"
                }

                override fun provideDownload(): CharSequence {
                    return "Đã tải: ${from.download ?: ""}"
                }

                override fun provideProductName(): CharSequence {
                    return "Tên sản phẩm: ${from.productName ?: ""}"
                }

                override fun provideCode(): CharSequence {
                    return "Mã lô: ${from.code ?: ""}"
                }
            }
        }
    }
}