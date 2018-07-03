package ishopgo.com.exhibition.ui.main.shop.news

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.post.PostObject
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.extensions.asHtml
import kotlinx.android.synthetic.main.item_booth_post.view.*

class NewsAdapter : ClickableAdapter<PostObject>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_booth_post
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

    interface PostProvider {
        fun provideAvatar(): CharSequence
        fun provideTitle(): CharSequence
        fun provideTime(): CharSequence
        fun provideOwner(): CharSequence
        fun provideShortDescription(): CharSequence
        fun provideCategory(): CharSequence
    }

    class PostConverter : Converter<PostObject, PostProvider> {

        override fun convert(from: PostObject): PostProvider {
            return object : PostProvider {
                override fun provideOwner(): CharSequence {
                    return from.accountName ?: ""
                }

                override fun provideCategory(): CharSequence {
                    return from.categoryName ?: ""
                }

                override fun provideAvatar(): CharSequence {
                    return from.image ?: ""
                }

                override fun provideTitle(): CharSequence {
                    return from.name ?: ""
                }

                override fun provideShortDescription(): CharSequence {
                    return from.shortContent?.asHtml() ?: ""
                }

                override fun provideTime(): CharSequence {
                    return from.createdAt?.asDate() ?: ""
                }

            }
        }
    }
}