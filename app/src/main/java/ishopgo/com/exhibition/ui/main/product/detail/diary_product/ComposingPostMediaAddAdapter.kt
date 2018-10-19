package ishopgo.com.exhibition.ui.main.product.detail.diary_product

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_image_horizontal.view.*

class ComposingPostMediaAddAdapter : ClickableAdapter<PostMedia>() {
    companion object {
        const val IMAGE_ADD = 0
        const val IMAGES_LIST = 1
        const val IMAGE_DELETE = 2
    }

    override fun getChildLayoutResource(viewType: Int): Int {
        return if (viewType == IMAGE_ADD) R.layout.item_image_add else R.layout.item_image_horizontal
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) IMAGE_ADD else IMAGES_LIST
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<PostMedia> {
        return if (viewType == IMAGE_ADD) AddHolder(v) else Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<PostMedia>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is AddHolder) {
            holder.apply {
                itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), IMAGE_ADD) }
            }
        } else if (holder is Holder) {
            holder.apply {
                itemView.btn_delete.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), IMAGE_DELETE) }
            }
        }
    }

    internal inner class AddHolder(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<PostMedia>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: PostMedia) {
            super.populate(data)
        }
    }

    internal inner class Holder(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<PostMedia>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: PostMedia) {
            super.populate(data)
            itemView.apply {
                iv_image.setBackgroundColor(Color.TRANSPARENT)
                Glide.with(context)
                        .load(data.uri)
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                        .into(iv_image)
            }
        }
    }

}