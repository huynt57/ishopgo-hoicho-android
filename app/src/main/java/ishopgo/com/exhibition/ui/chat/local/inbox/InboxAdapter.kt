package ishopgo.com.exhibition.ui.chat.local.inbox

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.asHtml
import kotlinx.android.synthetic.main.item_inbox.view.*

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class InboxAdapter : ClickableAdapter<InboxProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_inbox
    }

    fun indexOf(conversationId: String): Int {
        return mData.indexOfFirst {
            if (it is LocalConversationItem)
                return@indexOfFirst it.idConversions == conversationId
            return@indexOfFirst false
        }
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<InboxProvider> {
        return InboxHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<InboxProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    inner class InboxHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<InboxProvider>(v) {
        @SuppressLint("SetTextI18n")
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
                if (data.provideWasRead()) {
                    view_title.text = data.provideName()
                    view_message.text = data.provideMessage()
                } else {
                    view_title.text = "<b>${data.provideName()}</b>".asHtml()
                    view_message.text = "<b>${data.provideMessage()}</b>".asHtml()
                }
                view_time.text = data.provideTime()
                view_unread_count.visibility = if (data.provideWasRead()) View.GONE else View.VISIBLE
            }
        }
    }

}