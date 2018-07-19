package ishopgo.com.exhibition.ui.main.home.search.provider

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
import kotlinx.android.synthetic.main.item_search_brands.view.*

class SearchBrandsAdapter(private var itemWidthRatio: Float = -1f, private var itemHeightRatio: Float = -1F) : ClickableAdapter<Brand>() {

    private var screenWidth: Int = UserDataManager.displayWidth
    private var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_search_brands
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Brand> {
        val brandHolder = BrandHolder(v, ConverterSearchBrand())
        val layoutParams = brandHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()

        return brandHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<Brand>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is BrandHolder) {
            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                listener?.click(adapterPosition, getItem(adapterPosition))

            }
        }
    }

    internal inner class BrandHolder(view: View, private val converter: Converter<Brand, SearchBrandProvider>) : BaseRecyclerViewAdapter.ViewHolder<Brand>(view) {

        override fun populate(data: Brand) {
            super.populate(data)

            val converted = converter.convert(data)
            itemView.apply {
                Glide.with(itemView.context).load(converted.provideImage())
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        )
                        .into(iv_thumb)
                tv_item_name.text = converted.provideName()
            }
        }
    }

    interface SearchBrandProvider {
        fun provideImage(): String
        fun provideName(): String
    }

    internal class ConverterSearchBrand : Converter<Brand, SearchBrandProvider> {

        override fun convert(from: Brand): SearchBrandProvider {
            return object : SearchBrandProvider {
                override fun provideImage(): String {
                    return from.logo ?: ""
                }

                override fun provideName(): String {
                    return from.name ?: ""
                }
            }
        }

    }
}