package ishopgo.com.exhibition.ui.main.home.search.product

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asMoney
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
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
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
                view_price.text = converted.providePrice()
            }
        }
    }

    interface SearchProductProvider {
        fun provideImage(): String
        fun provideName(): String
        fun provideCode(): String
        fun providePrice(): CharSequence
    }

    internal class ConverterSearchProduct : Converter<Product, SearchProductProvider> {

        override fun convert(from: Product): SearchProductProvider {
            return object : SearchProductProvider {
                override fun providePrice(): CharSequence {
                    return if (from.price == 0L) {
                        "<b><font color=\"#00c853\">Liên hệ</font></b>".asHtml()
                    } else if (from.promotionPrice != null && from.promotionPrice != from.price) {
                        if (from.promotionPrice == 0L) // gia khuyen mai = 0 thi coi nhu ko khuyen mai
                            "<b><font color=\"#00c853\">${from.price.asMoney()}</font></b>".asHtml()
                        else
                            "<b><font color=\"#BDBDBD\"><strike>${from.price.asMoney()}</strike></font> <font color=\"#00c853\">${from.promotionPrice.asMoney()}</font></b> ".asHtml()
                    } else {
                        "<b><font color=\"#00c853\">${from.price.asMoney()}</font></b>".asHtml()
                    }
                }

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