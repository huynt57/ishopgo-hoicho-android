package ishopgo.com.exhibition.ui.main.brand

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_highlight_brand_item.view.*

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class HighlightBrandAdapter : ClickableAdapter<HighlightBrandProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_highlight_brand_item
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<HighlightBrandProvider> {
        return BrandHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<HighlightBrandProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    inner class BrandHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<HighlightBrandProvider>(v) {

        override fun populate(data: HighlightBrandProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(context)
                        .load(data.provideImage())
                        .apply(RequestOptions().placeholder(R.drawable.image_placeholder))
                        .into(view_image)
            }
        }
    }

}