package ishopgo.com.exhibition.ui.main.brandmanager

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_brand_manager.view.*

class BrandManagerAdapter : ClickableAdapter<Brand>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_brand_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Brand> {
        return Holder(v, BrandManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<Brand>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), BRAND_EDIT_CLICK) }
//            itemView.brand_item_highlight.setOnCheckedChangeListener { _, _ -> listener?.click(adapterPosition, getItem(adapterPosition), BRAND_FEATURED_CLICK) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<Brand, BrandManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<Brand>(v) {

        override fun populate(data: Brand) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(this).load(convert.provideLogo())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).into(brand_item_picture)
                brand_item_name.text = convert.provideName()
//                brand_item_highlight.isChecked = convert.provideIsFeatured()
            }
        }

    }

    companion object {
        const val BRAND_EDIT_CLICK = 1
        const val BRAND_FEATURED_CLICK = 2
        const val IS_FEATURED: Int = 1  //Thương hiệu nổi bật
    }

    interface BrandManagerProvider {
        fun provideName(): String
        fun provideLogo(): String
        fun provideIsFeatured(): Boolean
    }

    class BrandManagerConverter : Converter<Brand, BrandManagerProvider> {
        override fun convert(from: Brand): BrandManagerProvider {
            return object : BrandManagerProvider {
                override fun provideName(): String {
                    return from.name ?: ""
                }

                override fun provideLogo(): String {
                    return from.logo ?: ""
                }

                override fun provideIsFeatured(): Boolean {
                    return wasIsFeatured()
                }

                private fun wasIsFeatured() = from.isFeatured == IS_FEATURED
            }
        }
    }
}