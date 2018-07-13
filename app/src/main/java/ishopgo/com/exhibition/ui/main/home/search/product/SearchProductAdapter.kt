package ishopgo.com.exhibition.ui.main.home.search.product

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_search_product.view.*

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class SearchProductAdapter(private var itemWidthRatio: Float = -1f, private var itemHeightRatio: Float = -1F) : ClickableAdapter<Product>() {

    private var screenWidth: Int = UserDataManager.displayWidth
    private var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_search_product
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Product> {
        val productHolder = ProductHolder(v, ConverterSearchProduct())
        val layoutParams = productHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()

        return productHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<Product>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ProductHolder) {
            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }
    }

    internal inner class ProductHolder(view: View, private val converter: Converter<Product, SearchProductProvider>) : BaseRecyclerViewAdapter.ViewHolder<Product>(view) {

        override fun populate(data: Product) {
            super.populate(data)

            val converted = converter.convert(data)
            itemView.apply {
                Glide.with(itemView.context).load(converted.provideImage())
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        )
                        .into(iv_thumb)
                view_name.text = converted.provideName()
                view_code.text = converted.provideCode()
            }
        }
    }

    interface SearchProductProvider {
        fun provideImage(): String
        fun provideName(): String
        fun provideCode(): String
    }

    internal class ConverterSearchProduct : Converter<Product, SearchProductProvider> {

        override fun convert(from: Product): SearchProductProvider {
            return object : SearchProductProvider {
                override fun provideImage(): String {
                    return from.image ?: ""
                }

                override fun provideName(): String {
                    return from.name ?: ""
                }

                override fun provideCode(): String {
                    return from.code ?: ""
                }

            }
        }

    }
}