package ishopgo.com.exhibition.ui.main.brandmanager

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_brand_manager.view.*

class BrandManagerAdapter : ClickableAdapter<BrandManagerProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_brand_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<BrandManagerProvider> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<BrandManagerProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), BRAND_EDIT_CLICK) }
            itemView.brand_item_highlight.setOnCheckedChangeListener { _, _ -> listener?.click(adapterPosition, getItem(adapterPosition), BRAND_FEATURED_CLICK) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<BrandManagerProvider>(v) {

        override fun populate(data: BrandManagerProvider) {
            super.populate(data)

            itemView.apply {
                Glide.with(this).load(data.provideLogo())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(brand_item_picture)
                brand_item_name.text = data.provideName()
                brand_item_highlight.isChecked = data.provideIsFeatured()
            }
        }

    }

    companion object {
        const val BRAND_EDIT_CLICK = 1
        const val BRAND_FEATURED_CLICK = 2
    }
}