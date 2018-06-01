package ishopgo.com.exhibition.ui.main.ticket

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.BaseFragment
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
                Glide.with(context)
                        .load(it.provideBanner())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                        .into(view_image)
                tv_fair.text = it.provideTicketName()
                tv_user_name.text = it.provideName()
                tv_user_email.text = it.provideEmail()
                tv_user_phone.text = it.providePhone()
                tv_ticket_time.text = it.provideCreateAt()
                tv_ticket_address.text = it.provideAddress()
                tv_ticket_code.text = it.provideCode()
                img_qr_code.setImageBitmap(QRCode.from(it.provideCode()).withSize(300, 300).bitmap())
            }
        })
        viewModel.createTicket()
    }

}