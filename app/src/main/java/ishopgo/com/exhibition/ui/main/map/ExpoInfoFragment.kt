package ishopgo.com.exhibition.ui.main.map

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.Ticket
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import kotlinx.android.synthetic.main.fragment_expo_map_info.*

class ExpoInfoFragment : BaseFragment() {
    private var fairId = -1L
    private lateinit var shareViewModel: ExpoMapShareViewModel

    companion object {

        fun newInstance(params: Bundle): ExpoInfoFragment {
            val fragment = ExpoInfoFragment()
            fragment.arguments = params

            return fragment
        }

        const val ANIMATION_DURATION = 500
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_expo_map_info, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fairId = arguments?.getLong(Const.TransferKey.EXTRA_ID, -1L) ?: -1L

        val json = arguments?.getString(Const.TransferKey.EXTRA_JSON)
        expoInfo = Toolbox.gson.fromJson(json, ExpoConfig::class.java)
    }

    private var expoInfo: ExpoConfig? = null
    private lateinit var viewModel: ExpoDetailViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        shareViewModel = obtainViewModel(ExpoMapShareViewModel::class.java, true)

        viewModel = obtainViewModel(ExpoDetailViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                resolveError(it)
                hideProgressDialog()
            }
        })

        viewModel.expoDetail.observe(this, expoDetailObserver)


        if (fairId != -1L)
            startWithId(fairId)
        else {
            if (expoInfo != null) {
                requireNotNull(expoInfo)
                setupWithConfig()
            }
        }

        viewModel.ticket.observe(this, ticketObserver)
    }

    @SuppressLint("SetTextI18n", "SetJavaScriptEnabled")
    private fun setupWithConfig() {
//        view_introduce.setOnClickListener {
//            val intent = Intent(context, FullDetailActivity::class.java)
//            intent.putExtra(Const.TransferKey.EXTRA_JSON, expoInfo.description)
//            startActivity(intent)
//        }

        btn_get_ticket.setOnClickListener {
            showProgressDialog()
            viewModel.getTicket(expoInfo?.id ?: -1L)
        }

        view_zoom.setOnClickListener {
            val intent = Intent(context, PhotoAlbumViewActivity::class.java)
            intent.putExtra(Const.TransferKey.EXTRA_STRING_LIST, arrayOf(expoInfo?.map ?: ""))
            intent.putExtra(Const.TransferKey.EXTRA_FETCH_FULL_SIZE, true)
            startActivity(intent)
        }

//        view_fair_qrcode.setOnClickListener {
//            val extra = Bundle()
//            extra.putString(Const.TransferKey.EXTRA_JSON, Toolbox.gson.toJson(expoInfo))
//            Navigation.findNavController(requireActivity(), R.id.nav_map_host_fragment).navigate(R.id.action_expoDetailFragment_to_qrCodeExpo, extra)
//        }

        view_name.text = expoInfo?.name ?: ""
        view_time.text = "Thời gian: ${expoInfo?.startTime?.asDateTime()
                ?: ""} - ${expoInfo?.endTime?.asDateTime() ?: ""}"
        view_address.text = "Địa điểm: ${expoInfo?.address ?: ""}"
        view_price.text = "Giá vé: ${if (expoInfo?.price != 0L) expoInfo?.price?.asMoney()
                ?: "Miễn phí" else "Miễn phí"}"

        Glide.with(requireContext())
                .load(expoInfo?.map ?: "")
                .apply(RequestOptions().centerCrop()
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder)
                )
                .into(view_image)

        if (!TextUtils.isEmpty(expoInfo?.description ?: "")) {
            val fullHtml = String.format(
                    "<html><head><meta name=\"viewport\"/><style>%s</style></head><body>%s</body></html>",
                    Const.webViewCSS,
                    expoInfo?.description ?: ""
            )
            view_webview.loadData(fullHtml, "text/html; charset=UTF-8", null)
        }
        view_webview.settings.javaScriptEnabled = true
        view_webview.isNestedScrollingEnabled = false
        view_webview.isVerticalScrollBarEnabled = false
        view_webview.isHorizontalScrollBarEnabled = false
    }

    private fun startWithId(fairId: Long) {
        viewModel.loadExpoDetail(fairId)
    }

    private val ticketObserver = Observer<Ticket> { t ->
        t?.let {
            hideProgressDialog()
            shareViewModel.openTicketDetail(it)
        }
    }

    private val expoDetailObserver = Observer<ExpoConfig> {
        it?.let {
            expoInfo = it

            setupWithConfig()

        }
    }
}