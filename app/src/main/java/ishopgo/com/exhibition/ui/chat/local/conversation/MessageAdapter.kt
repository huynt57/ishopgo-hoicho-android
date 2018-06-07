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
import ishopgo.com.exhibition.model.chat.ChatProductMessage
import ishopgo.com.exhibition.model.chat.ChatTextMessage
import ishopgo.com.exhibition.model.chat.IChatMessage
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.photoview.PhotoAlbumViewActivity
import kotlinx.android.synthetic.main.stream_item_center.view.*
import kotlinx.android.synthetic.main.stream_item_left.view.*
import kotlinx.android.synthetic.main.stream_item_left_product.view.*
import kotlinx.android.synthetic.main.stream_item_right.view.*
import kotlinx.android.synthetic.main.stream_item_right_product.view.*

/**
 * Created by xuanhong on 11/18/17. HappyCoding!
 */
class MessageAdapter : ClickableAdapter<IChatMessage>() {

    companion object {
        const val TYPE_LEFT = 0
        const val TYPE_RIGHT = 1
        const val TYPE_CENTER = 2
        const val TYPE_LEFT_PRODUCT = 3
        const val TYPE_RIGHT_PRODUCT = 4

        const val CODE_NORMAL = 1
        const val CODE_FAIL = 2
        const val CODE_PRODUCT = 3
    }

    var currentId = UserDataManager.currentUserId

    override fun getChildLayoutResource(viewType: Int): Int {
        return when (viewType) {
            TYPE_CENTER -> {
                R.layout.stream_item_center
            }
            TYPE_LEFT -> {
                R.layout.stream_item_left
            }
            TYPE_LEFT_PRODUCT -> {
                R.layout.stream_item_left_product
            }
            TYPE_RIGHT_PRODUCT -> {
                R.layout.stream_item_right_product
            }
            else -> {
                R.layout.stream_item_right
            }
        }
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<IChatMessage> {
        return when (viewType) {
            TYPE_CENTER -> SystemMessageHolder(v)
            TYPE_LEFT -> MessageHolderLeft(v)
            TYPE_LEFT_PRODUCT -> LeftProductMessageHolder(v)
            TYPE_RIGHT_PRODUCT -> RightProductMessageHolder(v)
            else -> MessageHolderRight(v)
        }
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
        return when {
            data.getMessageType() == IChatMessage.TYPE_SYSTEM -> TYPE_CENTER
            data.getMessageType() == IChatMessage.TYPE_PRODUCT ->
                if (data.getOwnerId() == currentId)
                    TYPE_RIGHT_PRODUCT
                else
                    TYPE_LEFT_PRODUCT
            data.getOwnerId() == currentId -> TYPE_RIGHT
            else -> TYPE_LEFT
        }
    }

    override fun onBindViewHolder(holder: ViewHolder<IChatMessage>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                listener?.click(adapterPosition, getItem(adapterPosition), CODE_NORMAL)
            }
        }


        if (holder is LeftProductMessageHolder) {
            holder.itemView.apply {
                val adapterPosition = holder.adapterPosition
                listener?.click(adapterPosition, getItem(adapterPosition), CODE_PRODUCT)
            }
        }

        if (holder is RightProductMessageHolder) {
            holder.itemView.apply {
                val adapterPosition = holder.adapterPosition
                listener?.click(adapterPosition, getItem(adapterPosition), CODE_PRODUCT)
            }
        }

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
                    val adapterPosition = holder.adapterPosition
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
                    val adapterPosition = holder.adapterPosition
                    listener?.click(adapterPosition, getItem(adapterPosition), CODE_FAIL)
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
                    val adapterPosition = holder.adapterPosition
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
            }

        }
    }

    inner class RightProductMessageHolder(view: View) : BaseRecyclerViewAdapter.ViewHolder<IChatMessage>(view) {

        override fun populate(data: IChatMessage) {
            super.populate(data)
            itemView.apply {
                if (data is ChatProductMessage) {
                    data.getEmbededProduct()?.let {
                        Glide.with(context)
                                .load(it.image)
                                .apply(RequestOptions().placeholder(R.drawable.image_placeholder)
                                        .error(R.drawable.image_placeholder))
                                .into(right_productImage)
                        right_productName.text = it.provideName()
                        right_productPrice.text = it.providePrice()
                    }

                }

                right_product_time.text = data.getCreatedTime()
                // time will not display by default
                right_product_time.visibility = View.GONE

                when (data.getSendStatus()) {
                    IChatMessage.STATUS_SENDING -> {
                        right_product_view_container_progress.visibility = View.VISIBLE
                        right_product_view_container_error.visibility = View.GONE
                        right_product_view_progress.visibility = View.VISIBLE
                    }
                    IChatMessage.STATUS_SENT -> {
                        right_product_view_container_progress.visibility = View.GONE
                    }
                    IChatMessage.STATUS_FAILED -> {
                        right_product_view_container_progress.visibility = View.VISIBLE
                        right_product_view_container_error.visibility = View.VISIBLE
                        right_product_view_progress.visibility = View.GONE
                    }
                    else -> {
                        right_product_view_container_progress.visibility = View.GONE
                    }
                }
            }

        }

    }

    inner class LeftProductMessageHolder(view: View) : BaseRecyclerViewAdapter.ViewHolder<IChatMessage>(view) {

        override fun populate(data: IChatMessage) {
            super.populate(data)
            itemView.apply {
                if (data is ChatProductMessage) {
                    data.getEmbededProduct()?.let {
                        Glide.with(context)
                                .load(it.image)
                                .apply(RequestOptions().placeholder(R.drawable.image_placeholder)
                                        .error(R.drawable.image_placeholder))
                                .into(left_productImage)
                        left_productName.text = it.provideName()
                        left_productPrice.text = it.providePrice()
                    }

                }

                Glide.with(itemView).load(data.getOwnerAvatar())
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder)
                                .transforms(CenterCrop(), RoundedCorners(25))
                        )
                        .into(left_product_imageView)

                left_product_time.text = data.getCreatedTime()
                // time will not display by default
                left_product_time.visibility = View.GONE

                left_product_name.text = data.getOwnerName()

                val shouldShowTitle = shouldShowTitle(adapterPosition)
                left_product_name.visibility = if (shouldShowTitle) View.VISIBLE else View.GONE
                left_product_imageView.visibility = if (shouldShowTitle) View.VISIBLE else View.INVISIBLE
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

    inner class SystemMessageHolder(view: View) : BaseRecyclerViewAdapter.ViewHolder<IChatMessage>(view) {

        override fun populate(data: IChatMessage) {
            super.populate(data)
            itemView.apply {
                if (data is ChatTextMessage) {
                    center_textContent.text = data.getText()
                    center_textContent.visibility = if (data.getText().isBlank()) View.GONE else View.VISIBLE
                }

                center_time.text = data.getCreatedTime()
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
                                        .error(R.drawable.avatar_placeholder)
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
                                .error(R.drawable.avatar_placeholder)
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

    inner class MessageHolderRight(view: View) : BaseRecyclerViewAdapter.ViewHolder<IChatMessage>(view) {

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
                                        .error(R.drawable.image_placeholder)
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