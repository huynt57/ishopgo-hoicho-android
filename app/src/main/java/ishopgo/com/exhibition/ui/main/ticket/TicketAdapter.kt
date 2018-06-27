package ishopgo.com.exhibition.ui.main.ticket

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import kotlinx.android.synthetic.main.item_ticket.view.*
import net.glxn.qrgen.android.QRCode

/**
 * Created by xuanhong on 6/27/18. HappyCoding!
 */
class TicketAdapter : ClickableAdapter<Ticket>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_ticket
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Ticket> {
        return TicketHolder(v, ConverterTicket())
    }

    override fun onBindViewHolder(holder: ViewHolder<Ticket>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    class TicketHolder(v: View, private val converter: Converter<Ticket, TicketProvider>) : BaseRecyclerViewAdapter.ViewHolder<Ticket>(v) {

        override fun populate(data: Ticket) {
            super.populate(data)

            val converted = converter.convert(data)

            itemView.apply {
                ticket_code.setImageBitmap(QRCode.from(converted.qrCode().toString()).withSize(250, 250).bitmap())
                fair_title.text = converted.fairName()
                created_at.text = converted.createdAt()
            }

        }
    }

    interface TicketProvider {
        fun qrCode(): CharSequence
        fun fairName(): CharSequence
        fun createdAt(): CharSequence
    }

    class ConverterTicket : Converter<Ticket, TicketProvider> {

        override fun convert(from: Ticket): TicketProvider {
            return object : TicketProvider {
                override fun qrCode(): CharSequence {
                    return from.code ?: ""
                }

                override fun fairName(): CharSequence {
                    return from.fair?.name ?: ""
                }

                override fun createdAt(): CharSequence {
                    return "Tạo lúc: ${from.createdAt?.asDateTime() ?: ""}"
                }

            }
        }

    }
}