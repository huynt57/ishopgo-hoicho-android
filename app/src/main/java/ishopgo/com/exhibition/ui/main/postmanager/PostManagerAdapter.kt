package ishopgo.com.exhibition.ui.main.postmanager

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.post.PostObject
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
import kotlinx.android.synthetic.main.item_new_manager.view.*

class PostManagerAdapter : ClickableAdapter<PostObject>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_new_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<PostObject> {
        return Holder(v, PostConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<PostObject>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<PostObject, PostProvider>) : BaseRecyclerViewAdapter.ViewHolder<PostObject>(v) {

        override fun populate(data: PostObject) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(convert.provideAvatar())
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(view_image)
                tv_news_title.text = convert.provideTitle()
                tv_news_info.text = "${convert.provideTime()} | Đăng bởi <b><font color=\"blue\">${convert.provideOwner()}</font></b>".asHtml()
                tv_news_category.text = convert.provideCategory()
                tv_short_desc.text = convert.provideShortDescription()
            }
        }

    }
}