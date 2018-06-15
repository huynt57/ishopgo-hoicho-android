package ishopgo.com.exhibition.ui.main.home.post

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.post.PostObject
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_latest_news_item.view.*

/**
 * Created by xuanhong on 6/13/18. HappyCoding!
 */
class LatestPostsAdapter(private var itemWidthRatio: Float = -1f, private var itemHeightRatio: Float = -1F) : ClickableAdapter<PostObject>() {

    private var screenWidth: Int = UserDataManager.displayWidth
    private var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_latest_news_item
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<PostObject> {
        val brandHolder = BrandHolder(v, PostConverter())

        val layoutParams = brandHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()
        return brandHolder
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewAdapter.ViewHolder<PostObject>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    inner class BrandHolder(v: View, private val converter: Converter<PostObject, LatestPostProvider>) : BaseRecyclerViewAdapter.ViewHolder<PostObject>(v) {

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
                tv_news_time.text = convert.provideTime()
                tv_short_desc.text = convert.provideShortDescription()
            }
        }
    }

}