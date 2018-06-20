package ishopgo.com.exhibition.ui.main.notification

import android.graphics.Typeface
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Notification
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_notification.view.*

/**
 * Created by hoangnh on 5/7/2018.
 */
class NotificationAdapter : ClickableAdapter<Notification>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_notification
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Notification> {
        return Holder(v, ConverterNotification())
    }

    override fun onBindViewHolder(holder: ViewHolder<Notification>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    inner class Holder(v: View, private val converter: ConverterNotification) : BaseRecyclerViewAdapter.ViewHolder<Notification>(v) {

        override fun populate(data: Notification) {
            super.populate(data)

            val converted = converter.convert(data)

            itemView.apply {
                Glide.with(context)
                        .load(converted.provideImage())
                        .apply(RequestOptions().circleCrop()
                                .placeholder(R.mipmap.ic_launcher_round)
                                .error(R.mipmap.ic_launcher_round))
                        .into(sdv_picture)
                tv_title.text = converted.provideContent()
                tv_short_desc.text = converted.provideSender()
                tv_timestamp.text = converted.provideCreatedAt()
                if (converted.provideWasRed()) {
                    tv_title.setTypeface(null, Typeface.NORMAL)
                } else {
                    tv_title.setTypeface(null, Typeface.BOLD)
                }
            }
        }

    }
}