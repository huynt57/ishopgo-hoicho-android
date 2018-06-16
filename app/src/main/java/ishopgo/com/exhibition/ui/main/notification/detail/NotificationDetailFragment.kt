package ishopgo.com.exhibition.ui.main.notification.detail

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Notification
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.main.notification.NotificationViewModel
import ishopgo.com.exhibition.ui.main.product.detail.fulldetail.WebViewFragment
import kotlinx.android.synthetic.main.fragment_notification_detail.*

/**
 * Created by hoangnh on 5/7/2018.
 */
class NotificationDetailFragment : BaseFragment() {

    private lateinit var viewModel: NotificationViewModel

    companion object {
        fun newInstance(data: Bundle): NotificationDetailFragment {
            val fragment = NotificationDetailFragment()
            fragment.arguments = data

            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = obtainViewModel(NotificationViewModel::class.java, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notificationId = arguments?.getLong(Const.TransferKey.EXTRA_ID)
        notificationId?.let {
            viewModel.markAsRead(it)
            viewModel.getNotificationDetail(it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.notificationDetail.observe(this, Observer { n ->
            n?.let {
                showDetail(it)
            }
        })
    }

    private fun showDetail(notification: Notification) {
        val converted = ConverterNotificationDetail().convert(notification)

        Glide.with(context)
                .load(converted.provideImage())
                .apply(RequestOptions().circleCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round))
                .into(sdv_picture)
        tv_title.text = converted.provideContent()
        tv_short_desc.text = converted.provideSender()
        tv_timestamp.text = converted.provideCreatedAt()

        val argument = Bundle()
        argument.putString(Const.TransferKey.EXTRA_JSON, converted.provideWebContent())
        childFragmentManager.beginTransaction()
                .replace(R.id.view_web_content, WebViewFragment.newInstance(argument))
                .commit()
    }
}