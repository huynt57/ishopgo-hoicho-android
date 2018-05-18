package ishopgo.com.exhibition.ui.chat.local.conversation

import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.chat.ChatImageMessage
import ishopgo.com.exhibition.model.chat.ChatTextMessage
import ishopgo.com.exhibition.model.chat.IChatMessage
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import kotlinx.android.synthetic.main.stream_item_left.view.*
import kotlinx.android.synthetic.main.stream_item_right.view.*

/**
 * Created by xuanhong on 11/18/17. HappyCoding!
 */
class MessageAdapter : ClickableAdapter<IChatMessage>() {

    companion object {
        const val TYPE_LEFT = 0
        const val TYPE_RIGHT = 1
        const val CODE_NORMAL = 1
        const val CODE_FAIL = 2

    }

    var currentId = UserDataManager.currentUserId

    override fun getChildLayoutResource(viewType: Int): Int {
        return when (viewType) {
            TYPE_LEFT -> {
                R.layout.stream_item_left
            }
            else -> {
                R.layout.stream_item_right
            }
        }
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<IChatMessage> {
        return if (viewType == TYPE_LEFT) MessageHolderLeft(v) else MessageHolderRight(v)
    }


    fun update(message: IChatMessage) {
        for (i in (mData.size - 1) downTo 0) {
            val item = mData[i]
            if (item.getMessageId() == message.getMessageId()) {
                mData[i] = message
                notifyItemChanged(i)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItem(position)
        return if (data.getOwnerId() == currentId)
            TYPE_RIGHT
        else
            TYPE_LEFT
    }

    override fun onBindViewHolder(holder: ViewHolder<IChatMessage>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener {
                listener?.click(adapterPosition, getItem(adapterPosition), CODE_NORMAL)
            }
        }

        val adapterPosition = holder.adapterPosition

        if (holder is MessageHolderRight) {
            holder.itemView.apply {
                right_constraintLayout.setOnClickListener {
                    val isVisible = holder.itemView.right_time.visibility == View.VISIBLE
                    holder.itemView.right_time.visibility = if (isVisible) View.GONE else View.VISIBLE
                }
                right_textContent.setOnClickListener {
                    val isVisible = holder.itemView.right_time.visibility == View.VISIBLE
                    holder.itemView.right_time.visibility = if (isVisible) View.GONE else View.VISIBLE
                }
                right_imageContent.setOnClickListener {
                    val item = getItem(adapterPosition)
                    if (item is ChatImageMessage) {
                        val imageUrls = item.getImageUrls()
                        if (imageUrls.isNotEmpty()) {
                            val i = Intent(it.context, PhotoAlbumViewActivity::class.java)
                            i.putExtra(Const.TransferKey.EXTRA_STRING_LIST, imageUrls.toTypedArray())
                            it.context.startActivity(i)
                        }
                    }
                }
                right_view_container_error.setOnClickListener {
                    val pos = adapterPosition
                    listener?.click(pos, getItem(pos), CODE_FAIL)
                }
            }

        }

        if (holder is MessageHolderLeft) {
            holder.itemView.apply {
                left_constraintLayout.setOnClickListener {
                    val isVisible = holder.itemView.left_time.visibility == View.VISIBLE
                    holder.itemView.left_time.visibility = if (isVisible) View.GONE else View.VISIBLE
                }
                left_textContent.setOnClickListener {
                    val isVisible = holder.itemView.left_time.visibility == View.VISIBLE
                    holder.itemView.left_time.visibility = if (isVisible) View.GONE else View.VISIBLE
                }
                left_imageContent.setOnClickListener {
                    val item = getItem(holder.adapterPosition)
                    if (item is ChatImageMessage) {
                        val imageUrls = item.getImageUrls()
                        if (imageUrls.isNotEmpty()) {
                            val i = Intent(it.context, PhotoAlbumViewActivity::class.java)
                            i.putExtra(Const.TransferKey.EXTRA_STRING_LIST, imageUrls.toTypedArray())
                            it.context.startActivity(i)
                        }
                    }
                }
            }

        }
    }

    inner class MessageHolderLeft(view: View) : BaseRecyclerViewAdapter.ViewHolder<IChatMessage>(view) {

        override fun populate(data: IChatMessage) {
            super.populate(data)
            itemView.apply {
                if (data is ChatTextMessage) {
                    left_textContent.text = data.getText()
                    left_textContent.visibility = if (data.getText().isBlank()) View.GONE else View.VISIBLE
                }

                if (data is ChatImageMessage) {
                    val imageUrls = data.getImageUrls()
                    left_image_container.visibility = if (imageUrls.isNotEmpty()) View.VISIBLE else View.GONE
                    if (imageUrls.isNotEmpty()) {
                        Glide.with(itemView).load(imageUrls[0])
                                .apply(RequestOptions
                                        .placeholderOf(R.drawable.avatar_placeholder)
                                        .transforms(CenterCrop(), RoundedCorners(25))
                                )
                                .into(left_imageContent)

                        left_imageContent_number.visibility = if (imageUrls.size > 1) View.VISIBLE else View.GONE
                        left_imageContent_number.text = "+ ${imageUrls.size - 1}"

                        // priority image
                        left_textContent.visibility = View.GONE
                        left_constraintLayout.setBackgroundResource(0)
                    } else {
                        left_constraintLayout.setBackgroundResource(R.drawable.bg_buble_left)
                    }
                }

                Glide.with(itemView).load(data.getOwnerAvatar())
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.avatar_placeholder)
                                .transforms(CenterCrop(), RoundedCorners(25))
                        )
                        .into(left_imageView)

                left_time.text = data.getCreatedTime()
                // time will not display by default
                left_time.visibility = View.GONE

                left_name.text = data.getOwnerName()

                val shouldShowTitle = shouldShowTitle(adapterPosition)
                left_name.visibility = if (shouldShowTitle) View.VISIBLE else View.GONE
                left_imageView.visibility = if (shouldShowTitle) View.VISIBLE else View.INVISIBLE
            }

        }

        private fun shouldShowTitle(position: Int): Boolean {
            val current = getItem(position)
            val beforeIndex = position + 1 // because of reverse layout
            if (beforeIndex < itemCount) {
                val item = getItem(beforeIndex)
                if (item.getOwnerId() != current.getOwnerId()) {
                    // message come from other person, should show name
                    return true
                }
            } else {
                // first message
                return true
            }

            return false
        }

    }

    class MessageHolderRight(view: View) : BaseRecyclerViewAdapter.ViewHolder<IChatMessage>(view) {

        override fun populate(data: IChatMessage) {
            super.populate(data)

            itemView.apply {
                if (data is ChatTextMessage) {
                    right_textContent.text = data.getText()
                    right_textContent.visibility = if (data.getText().isBlank()) View.GONE else View.VISIBLE
                }

                if (data is ChatImageMessage) {
                    val imageUrls = data.getImageUrls()
                    right_image_container.visibility = if (imageUrls.isNotEmpty()) View.VISIBLE else View.GONE
                    if (imageUrls.isNotEmpty()) {
                        Glide.with(itemView.context).load(imageUrls[0])
                                .apply(RequestOptions()
                                        .placeholder(R.drawable.image_placeholder)
                                        .transforms(CenterCrop(), RoundedCorners(25))
                                )
                                .into(right_imageContent)

                        right_imageContent_number.visibility = if (imageUrls.size > 1) View.VISIBLE else View.GONE
                        right_imageContent_number.text = "+ ${imageUrls.size - 1}"

                        // priority image
                        right_textContent.visibility = View.GONE
                        right_constraintLayout.setBackgroundResource(0)
                    } else {
                        right_constraintLayout.setBackgroundResource(R.drawable.bg_buble_right)
                    }
                }

                right_time.text = data.getCreatedTime()
                // time will not display by default
                right_time.visibility = View.GONE

                when (data.getSendStatus()) {
                    IChatMessage.STATUS_SENDING -> {
                        right_view_container_progress.visibility = View.VISIBLE
                        right_view_container_error.visibility = View.GONE
                        right_view_progress.visibility = View.VISIBLE
                    }
                    IChatMessage.STATUS_SENT -> {
                        right_view_container_progress.visibility = View.GONE
                    }
                    IChatMessage.STATUS_FAILED -> {
                        right_view_container_progress.visibility = View.VISIBLE
                        right_view_container_error.visibility = View.VISIBLE
                        right_view_progress.visibility = View.GONE
                    }
                    else -> {
                        right_view_container_progress.visibility = View.GONE
                    }
                }
            }

        }

    }

}