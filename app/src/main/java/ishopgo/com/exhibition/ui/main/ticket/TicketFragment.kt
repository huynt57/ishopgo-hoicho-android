package ishopgo.com.exhibition.ui.main.ticket

import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.*
import kotlinx.android.synthetic.main.fragment_ticket_account.*
import net.glxn.qrgen.android.QRCode

class TicketFragment : BaseFragment() {
    private lateinit var ticket: Ticket

    companion object {
        fun newInstance(params: Bundle): TicketFragment {
            val fragment = TicketFragment()
            fragment.arguments = params

            return fragment
        }

        val PAYMENT_STATUS_FREE = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ticket_account, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        ticket = Toolbox.gson.fromJson(json, Ticket::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val converted = TicketConverter().convert(ticket)

        Glide.with(context)
                .load(converted.provideAvatar())
                .apply(RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.expo_ticket_background)
                        .error(R.drawable.expo_ticket_background)
                ).into(view_image)

        Glide.with(context)
                .load(converted.provideAvatarUser())
                .apply(RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder)
                ).into(image_avatar_user)

        tv_fair.text = converted.provideTicketName()
        tv_user_name.text = converted.provideName()
        tv_user_email.text = converted.provideEmail()
        tv_date_get_ticket.text = converted.providerDateGetTicket()
        tv_ticket_price.text = converted.providePrice()
        tv_ticket_payment_status.text = converted.providePaymentStatusText()
        tv_user_phone.setPhone(converted.providePhone(), ticket.phone ?: "")

        tv_ticket_time.text = converted.provideCreateAt()
        tv_ticket_address.text = converted.provideAddress()
        tv_ticket_code.text = converted.provideCode()
        img_qr_code.setImageBitmap(QRCode.from(converted.provideCode()).withSize(300, 300).bitmap())
    }

    interface TicketProvider {
        fun provideName(): CharSequence
        fun provideEmail(): CharSequence
        fun provideTicketName(): String
        fun provideCode(): String
        fun providePhone(): CharSequence
        fun provideAddress(): CharSequence
        fun provideCreateAt(): CharSequence
        fun providePrice(): CharSequence
        fun providePaymentStatusText(): CharSequence
        fun provideAvatar(): CharSequence
        fun provideAvatarUser(): CharSequence
        fun providerDateGetTicket(): CharSequence
    }

    class TicketConverter : Converter<Ticket, TicketProvider> {
        override fun convert(from: Ticket): TicketProvider {
            return object : TicketProvider {
                override fun provideAvatarUser(): CharSequence {
                    return from.avatar ?: ""
                }

                override fun providerDateGetTicket(): CharSequence {
                    return "Ngày lấy vé: <b>${from.createdAt?.asDateTime() ?: ""}</b>".asHtml()
                }

                override fun provideAvatar(): CharSequence {
                    return from.fair?.image ?: ""
                }

                override fun providePaymentStatusText(): CharSequence {
                    return "Trạng thái: <b><font color=\"#F44336\">${from.paymentStatusText}<font></b>".asHtml()
                }

                override fun providePrice(): CharSequence {

                    return "Giá vé: <b>${if (from.paymentStatus != PAYMENT_STATUS_FREE) from.fair?.price?.asMoney()
                            ?: "0đ" else "Miễn phí"}</b>".asHtml()
                }

                override fun provideEmail(): CharSequence {
                    return "Email: <b>${from.email ?: ""}</b>".asHtml()
                }

                override fun provideTicketName(): String {
                    return from.fair?.name ?: ""
                }

                override fun provideName(): CharSequence {
                    return "Họ và tên: <b>${from.name ?: ""}</b>".asHtml()
                }

                override fun provideCode(): String {
                    return from.code ?: ""
                }

                override fun providePhone(): CharSequence {
                    return "Số điện thoại: <b>${from.phone
                            ?: ""}</b>".asHtml()
                }

                override fun provideAddress(): CharSequence {
                    return "Địa chỉ: <b>${from.fair?.address ?: ""}</b>".asHtml()
                }

                override fun provideCreateAt(): CharSequence {
                    return "Thời gian: <b>${from.fair?.startTime?.asDateTime()} - ${from.fair?.endTime?.asDateTime()}".asHtml()
                }
            }
        }
    }
}