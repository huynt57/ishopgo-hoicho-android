package ishopgo.com.exhibition.ui.main.brand

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_highlight_brand_item.view.*

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class HighlightBrandAdapter(private var itemWidthRatio: Float = -1f, private var itemHeightRatio: Float = -1F) : ClickableAdapter<Brand>() {

    private var screenWidth: Int = UserDataManager.displayWidth
    private var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_highlight_brand_item
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Brand> {
        val brandHolder = BrandHolder(v, ConverterHighlightBrand())

        val layoutParams = brandHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()
        return brandHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<Brand>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    inner class BrandHolder(v: View, private val converter: Converter<Brand, HighlightBrandProvider>) : BaseRecyclerViewAdapter.ViewHolder<Brand>(v) {

        override fun populate(data: Brand) {
            super.populate(data)

            val converted = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(converted.provideImage())
                        .apply(RequestOptions()
                                .placeholder(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder))
                        .into(view_image)
            }
        }
    }

    interface HighlightBrandProvider {

        fun provideImage(): String
        fun provideName(): String

    }

    class ConverterHighlightBrand : Converter<Brand, HighlightBrandProvider> {

        override fun convert(from: Brand): HighlightBrandProvider {
            return object : HighlightBrandProvider {
                override fun provideName(): String {
                    return from.name ?: ""
                }

                override fun provideImage(): String {
                    return from.logo ?: ""
                }

            }
        }

    }

}