package ishopgo.com.exhibition.ui.main.scan.history

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.HistoryScan
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_history_qrcode.view.*

class HistoryQrCodeAdapter : ClickableAdapter<HistoryScan.QrCode>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_history_qrcode
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<HistoryScan.QrCode> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<HistoryScan.QrCode>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<HistoryScan.QrCode>(v) {

        override fun populate(data: HistoryScan.QrCode) {
            super.populate(data)

            itemView.apply {
                view_code.text = data.code
            }
        }
    }
}