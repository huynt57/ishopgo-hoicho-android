package ishopgo.com.exhibition.ui.main.ticketmanager

import android.content.Intent
import android.net.Uri
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import kotlinx.android.synthetic.main.item_ticket_manager.view.*
import net.glxn.qrgen.android.QRCode

class TicketManagerAdapter : ClickableAdapter<Ticket>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_ticket_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Ticket> {
        return Holder(v, TicketManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<Ticket>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.btn_save_ticket.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), SAVE_QRCODE_TO_STORAGE) }
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_ITEM_TO_PROFILE) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<Ticket, TicketManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<Ticket>(v) {

        override fun populate(data: Ticket) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                img_code.setImageBitmap(QRCode.from(convert.provideTicketCode()).withSize(300, 300).bitmap())

                tv_ticket_name.text = convert.provideBoothName()
                tv_ticket_address.text = convert.provideTicketAddress()
                tv_ticket_datescan.text = convert.provideDateScan()
                tv_ticket_phone.text = convert.provideBoothPhone()
                tv_ticket_phone.setOnClickListener {
                    val uri = Uri.parse("tel:${convert.provideBoothPhone()}")
                    val i = Intent(Intent.ACTION_DIAL, uri)
                    it.context.startActivity(i)
                }
            }
        }
    }

    companion object {
        const val SAVE_QRCODE_TO_STORAGE = 0
        const val CLICK_ITEM_TO_PROFILE = 1
    }

    interface TicketManagerProvider {
        fun provideBoothName(): String
        fun provideTicketAddress(): String
        fun provideTicketCode(): String
        fun provideBoothPhone(): String
        fun provideDateScan(): String
    }

    class TicketManagerConverter : Converter<Ticket, TicketManagerProvider> {
        override fun convert(from: Ticket): TicketManagerProvider {
            return object : TicketManagerProvider {
                override fun provideBoothName(): String {
                    return from.name ?: ""
                }

                override fun provideTicketAddress(): String {
                    return "Địa chỉ: ${from.address ?: ""}, ${from.district ?: ""}, ${from.city
                            ?: ""}"
                }

                override fun provideTicketCode(): String {
                    return from.code ?: ""
                }

                override fun provideBoothPhone(): String {
                    return from.phone ?: ""
                }

                override fun provideDateScan(): String {
                    return "Ngày quét: ${from.createdAt?.asDateTime() ?: ""}"
                }
            }
        }
    }
}