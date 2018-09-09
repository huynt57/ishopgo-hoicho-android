package ishopgo.com.exhibition.ui.main.product.icheckproduct

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IcheckProduct
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asMoney
import kotlinx.android.synthetic.main.item_product_grid.view.*

class IcheckProductAdapter(private var itemWidthRatio: Float = -1f, private var itemHeightRatio: Float = -1F) : ClickableAdapter<IcheckProduct>() {

    private var screenWidth: Int = UserDataManager.displayWidth
    private var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_grid
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<IcheckProduct> {
        val productHolder = ProductHolder(v, ProductConverter())
        val layoutParams = productHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()

        return productHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<IcheckProduct>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    internal inner class ProductHolder(view: View, private val converter: Converter<IcheckProduct, ProductProvider>) : BaseRecyclerViewAdapter.ViewHolder<IcheckProduct>(view) {

        override fun populate(data: IcheckProduct) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(itemView.context).load(convert.provideImage())
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.image_placeholder)
                                .error(R.drawable.image_placeholder)
                        )
                        .into(iv_thumb)
                tv_item_name.text = convert.provideName()

                tv_price.text = convert.providePrice()
                tv_tt_price.visibility = View.INVISIBLE
                tv_tt_price.text = convert.providePrice()
                view_discount.visibility = View.GONE
            }
        }

    }

    interface ProductProvider {
        fun provideImage(): String
        fun provideName(): String
        fun providePrice(): String
    }

    class ProductConverter : Converter<IcheckProduct, ProductProvider> {

        override fun convert(from: IcheckProduct): ProductProvider {
            return object : ProductProvider {
                override fun provideImage(): String {
                    val linkImage = from.imageDefault ?: ""
                    return if (linkImage.toLowerCase().startsWith("http")) linkImage else "http://ucontent.icheck.vn/" + linkImage + "_medium.jpg"
                }

                override fun provideName(): String {
                    return from.productName?.trim() ?: "Sản phẩm"
                }

                override fun providePrice(): String {
                    return if (from.priceDefault == 0.0) "Liên hệ"
                    else
                        return from.priceDefault.asMoney()
                }
            }
        }
    }
}
