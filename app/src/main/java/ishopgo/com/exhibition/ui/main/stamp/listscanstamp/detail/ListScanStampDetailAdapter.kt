package ishopgo.com.exhibition.ui.main.stamp.listscanstamp.detail

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.StampUserListScanDetail
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import kotlinx.android.synthetic.main.item_stamp_list_scan_detail.view.*

class ListScanStampDetailAdapter : ClickableAdapter<StampUserListScanDetail>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_stamp_list_scan_detail
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<StampUserListScanDetail> {
        return Holder(v, StampUserListScanConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<StampUserListScanDetail>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<StampUserListScanDetail, StampUserListScanProvider>) : BaseRecyclerViewAdapter.ViewHolder<StampUserListScanDetail>(v) {

        override fun populate(data: StampUserListScanDetail) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_stampName.text = convert.provideStampName()
                tv_stampDate.text = convert.provideDate()
                tv_stampProductName.text = convert.provideProductName()
                tv_stampCount.text = convert.provideCountScan()
                tv_stampNote.text = convert.provideNote()
            }
        }
    }

    interface StampUserListScanProvider {
        fun provideStampName(): CharSequence
        fun provideDate(): CharSequence
        fun provideProductName(): CharSequence
        fun provideCountScan(): CharSequence
        fun provideNote(): CharSequence
    }

    class StampUserListScanConverter : Converter<StampUserListScanDetail, StampUserListScanProvider> {
        override fun convert(from: StampUserListScanDetail): StampUserListScanProvider {
            return object : StampUserListScanProvider {
                override fun provideStampName(): CharSequence {
                    return "Mã tem: ${from.code ?: ""}"
                }

                override fun provideDate(): CharSequence {
                    return from.updatedAt?.asDateTime() ?: ""
                }

                override fun provideProductName(): CharSequence {
                    return "Tên sản phẩm: ${from.productName ?: ""}"
                }

                override fun provideCountScan(): CharSequence {
                    return "Số lần quét: ${from.countScan ?: "0"}"
                }

                override fun provideNote(): CharSequence {
                    return "Ghi chú: ${from.note ?: ""}"
                }
            }
        }
    }
}