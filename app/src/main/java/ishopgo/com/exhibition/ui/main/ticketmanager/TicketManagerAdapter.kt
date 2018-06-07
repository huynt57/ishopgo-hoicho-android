package ishopgo.com.exhibition.ui.main.ticketmanager

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_ticket_manager.view.*
import net.glxn.qrgen.android.QRCode

class TicketManagerAdapter : ClickableAdapter<TicketManagerProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_ticket_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<TicketManagerProvider> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<TicketManagerProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.btn_save_ticket.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<TicketManagerProvider>(v) {

        override fun populate(data: TicketManagerProvider) {
            super.populate(data)

            itemView.apply {
                img_code.setImageBitmap(QRCode.from(data.provideTicketCode()).withSize(300, 300).bitmap())

                tv_ticket_name.text = data.provideBoothName()
                tv_ticket_address.text = data.provideTicketAddress()
                tv_ticket_datescan.text = data.provideDateScan()
                tv_ticket_phone.text = data.provideBoothPhone()
            }
        }

    }
}