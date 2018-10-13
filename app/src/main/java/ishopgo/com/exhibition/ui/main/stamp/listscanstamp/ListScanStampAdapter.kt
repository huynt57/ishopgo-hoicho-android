package ishopgo.com.exhibition.ui.main.stamp.listscanstamp

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.StampUserListScan
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_stamp_list_scan.view.*

class ListScanStampAdapter : ClickableAdapter<StampUserListScan>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_stamp_list_scan
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<StampUserListScan> {
        return Holder(v, StampUserListScanConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<StampUserListScan>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<StampUserListScan, StampUserListScanProvider>) : BaseRecyclerViewAdapter.ViewHolder<StampUserListScan>(v) {

        override fun populate(data: StampUserListScan) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_stampName.text = convert.provideName()
                tv_stampSDT.setPhone(convert.provideSDT(), data.accountPhone ?: "")
                tv_stampEmail.text = convert.provideEmail()
                tv_stampRegion.text = convert.provideRegion()
                tv_stampCount.text = convert.provideCountScan()
            }
        }
    }

    interface StampUserListScanProvider {
        fun provideName(): CharSequence
        fun provideSDT(): CharSequence
        fun provideEmail(): CharSequence
        fun provideCountScan(): CharSequence
        fun provideRegion(): CharSequence
    }

    class StampUserListScanConverter : Converter<StampUserListScan, StampUserListScanProvider> {
        override fun convert(from: StampUserListScan): StampUserListScanProvider {
            return object : StampUserListScanProvider {
                override fun provideName(): CharSequence {
                    return "Họ và tên: ${from.accountName ?: ""}"
                }

                override fun provideSDT(): CharSequence {
                    return from.accountPhone ?: ""
                }

                override fun provideEmail(): CharSequence {
                    return "Email: ${from.location ?: ""}"
                }

                override fun provideRegion(): CharSequence {
                    return "Khu vực: ${from.location ?: ""}"
                }

                override fun provideCountScan(): CharSequence {
                    return "Số lượt quét: ${from.numberOfScans ?: 0}"
                }
            }
        }
    }
}