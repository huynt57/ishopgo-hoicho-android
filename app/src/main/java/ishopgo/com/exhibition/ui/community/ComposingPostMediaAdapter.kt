package ishopgo.com.exhibition.ui.community

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.PostMedia
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_image_horizontal.view.*

class ComposingPostMediaAdapter : ClickableAdapter<PostMedia>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_image_horizontal   }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<PostMedia> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<PostMedia>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.btn_delete.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
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