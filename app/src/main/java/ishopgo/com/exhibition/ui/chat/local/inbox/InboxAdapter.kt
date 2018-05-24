package ishopgo.com.exhibition.ui.chat.local.inbox

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_inbox.view.*

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class InboxAdapter : BaseRecyclerViewAdapter<InboxProvider>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_inbox
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<InboxProvider> {
        return InboxHolder(v)
    }

    inner class InboxHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<InboxProvider>(v) {
        override fun populate(data: InboxProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(context).load(data.provideAvatar())
                        .apply(RequestOptions()
                                .centerCrop()
                                .circleCrop()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder)
                        )
                        .into(view_avatar)
                view_title.text = data.provideName()
                view_time.text = data.provideTime()
                view_message.text = data.provideMessage()
                view_unread_count
            }
        }
    }

}