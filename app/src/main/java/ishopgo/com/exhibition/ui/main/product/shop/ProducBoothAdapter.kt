package ishopgo.com.exhibition.ui.main.product.shop

import android.graphics.Paint
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
import kotlinx.android.synthetic.main.item_product_booth_grid.view.*

class ProducBoothAdapter(private var itemWidthRatio: Float = -1f, private var itemHeightRatio: Float = -1F) : ClickableAdapter<Product>() {

    companion object {
        const val CLICK_ITEMVIEW = 1
        const val CLICK_ADD_PRODUCT = 2
    }

    private var screenWidth: Int = UserDataManager.displayWidth
    private var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_booth_grid
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Product> {
        val productHolder = ProductHolder(v, ProductConverter())
        val layoutParams = productHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()

        return productHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<Product>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            listener?.click(adapterPosition, getItem(adapterPosition), CLICK_ITEMVIEW)
        }

        holder.itemView.iv_addProduct.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            listener?.click(adapterPosition, getItem(adapterPosition), CLICK_ADD_PRODUCT)
        }
    }

    internal inner class ProductHolder(view: View, private val converter: Converter<Product, ProductProvider>) : BaseRecyclerViewAdapter.ViewHolder<Product>(view) {

        override fun populate(data: Product) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(itemView.context).load(data.image)
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        )
                        .into(iv_thumb)
                tv_item_name.text = convert.provideName()

                if (convert.hasDiscount()) {
                    tv_price.text = convert.providePromotionPrice()
                    tv_tt_price.visibility = View.VISIBLE
                    tv_tt_price.paintFlags = tv_tt_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tv_tt_price.text = convert.providePrice()

                    view_discount.visibility = View.VISIBLE
                    view_discount.text = convert.discountPercent()
                } else {
                    tv_price.text = convert.providePrice()
                    tv_tt_price.visibility = View.INVISIBLE
                    tv_tt_price.text = convert.providePrice()
                    view_discount.visibility = View.GONE
                }
            }

        }
    }

    interface ProductProvider {
        fun provideImage(): String
        fun provideName(): CharSequence
        fun providePrice(): String
        fun providePromotionPrice(): String
        fun hasDiscount(): Boolean
        fun discountPercent(): CharSequence
    }

    class ProductConverter : Converter<Product, ProductProvider> {

        override fun convert(from: Product): ProductProvider {
            return object : ProductProvider {
                override fun discountPercent(): CharSequence {
                    if (hasDiscount()) {
                        val pPrice = from.promotionPrice
                        val rPrice = from.price

                        if (rPrice == 0L) return "0%"
                        val percent = (1 - pPrice.toFloat() / rPrice.toFloat()) * 100

                        val displayed = if (percent.toInt() == 100) "<small><b><font color=\"red\">FREE</font></b></small>"
                        else "<small>GIẢM</small>" +
                                "<br>" +
                                "<b><font color=\"red\">${percent.toInt()}%</font></b>"

                        return displayed.asHtml()
                    } else
                        return "0%"
                }

                override fun hasDiscount(): Boolean {
                    return from.promotionPrice != 0L && from.promotionPrice < from.price
                }

                override fun provideImage(): String {
                    return from.image?.trim() ?: ""
                }

                override fun provideName(): CharSequence {
                    return "${from.name?.trim()
                            ?: "Sản phẩm"} - <b><font color=\"blue\">${from.departmentName?.trim()
                            ?: ""}</font></b>".asHtml()
                }

                override fun providePrice(): String {
                    return if (from.price == 0L) "Liên hệ"
                    else
                        return from.price.asMoney()
                }

                override fun providePromotionPrice(): String {
                    return from.promotionPrice.asMoney()
                }
            }
        }
    }
}