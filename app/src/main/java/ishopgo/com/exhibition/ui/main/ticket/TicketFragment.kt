package ishopgo.com.exhibition.ui.main.ticket

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asPhone
import kotlinx.android.synthetic.main.fragment_ticket_account.*
import net.glxn.qrgen.android.QRCode

class TicketFragment : BaseFragment() {
    private lateinit var viewModel: TicketViewModel

    companion object {
        fun newInstance(params: Bundle): TicketFragment {
            val fragment = TicketFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ticket_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(TicketViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.getTicketData.observe(this, Observer { p ->
            p?.let {
                val converted = TicketConverter().convert(it)

                Glide.with(context)
                        .load(converted.provideBanner())
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.expo_ticket_background)
                                .error(R.drawable.expo_ticket_background)
                        )
                        .into(view_image)
                tv_fair.text = converted.provideTicketName()
                tv_user_name.text = converted.provideName()
                tv_user_email.text = converted.provideEmail()
                tv_user_phone.text = converted.providePhone()
                tv_ticket_time.text = converted.provideCreateAt()
                tv_ticket_address.text = converted.provideAddress()
                tv_ticket_code.text = converted.provideCode()
                img_qr_code.setImageBitmap(QRCode.from(converted.provideCode()).withSize(300, 300).bitmap())
            }
        })
        viewModel.createTicket()
    }

    interface TicketProvider {
        fun provideName(): Spanned
        fun provideEmail(): Spanned
        fun provideTicketName(): String
        fun provideCode(): String
        fun providePhone(): Spanned
        fun provideAddress(): Spanned
        fun provideBanner(): String
        fun provideCreateAt(): Spanned
    }

    class TicketConverter : Converter<Ticket, TicketProvider> {
        override fun convert(from: Ticket): TicketProvider {
            return object : TicketProvider {
                override fun provideEmail(): Spanned {
                    return "Email: <b>${from.email ?: ""}</b>".asHtml()
                }

                override fun provideTicketName(): String {
                    return from.ticketName ?: ""
                }

                override fun provideName(): Spanned {
                    return "Họ và tên: <b>${from.name ?: ""}</b>".asHtml()
                }

                override fun provideCode(): String {
                    return from.code ?: ""
                }

                override fun providePhone(): Spanned {
                    return "Số điện thoại: <b>${from.phone?.asPhone() ?: ""}</b>".asHtml()
                }

                override fun provideAddress(): Spanned {
                    return "Địa chỉ: <b>${from.address ?: ""}, ${from.district ?: ""}, ${from.city
                            ?: ""}</b>".asHtml()
                }

                override fun provideBanner(): String {
                    return from.banner ?: ""
                }

                override fun provideCreateAt(): Spanned {
                    return "Thời gian: <b>${from.createdAt?.asDateTime() ?: ""}".asHtml()
                }
            }
        }
    }
}