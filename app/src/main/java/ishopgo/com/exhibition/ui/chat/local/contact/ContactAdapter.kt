package ishopgo.com.exhibition.ui.chat.local.contact

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.text.method.LinkMovementMethod
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_contact.view.*

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class ContactAdapter : ClickableAdapter<ContactProvider>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_contact
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ContactProvider> {
        return InboxHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<ContactProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    inner class InboxHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<ContactProvider>(v) {
        @SuppressLint("SetTextI18n")
        override fun populate(data: ContactProvider) {
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
                view_name.text = data.provideName()
                view_phone.text = "${data.provideJob()} - ${data.providePhone().setPhone(data.providePhone())}"
                view_phone.movementMethod = LinkMovementMethod.getInstance()

            }
        }
    }

}