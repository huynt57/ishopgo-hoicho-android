package ishopgo.com.exhibition.ui.main.notification

import android.graphics.Typeface
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.asDate
import kotlinx.android.synthetic.main.item_notification.view.*

/**
 * Created by hoangnh on 5/7/2018.
 */
class NotificationAdapter : ClickableAdapter<NotificationProvider>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_notification    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<NotificationProvider> {
        return Holder(v)
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<NotificationProvider>(v) {

        override fun populate(data: NotificationProvider) {
            super.populate(data)

            itemView.apply {
                tv_title.text = data.provideTitle()
                tv_short_desc.text = data.provideShortDescription()
                tv_timestamp.text = data.provideCreatedAt().asDate()
                if (data.provideIsRead() == 1) {
                    tv_title.setTypeface(null, Typeface.NORMAL)
                } else {
                    tv_title.setTypeface(null, Typeface.BOLD)
                }
            }
        }

    }
}