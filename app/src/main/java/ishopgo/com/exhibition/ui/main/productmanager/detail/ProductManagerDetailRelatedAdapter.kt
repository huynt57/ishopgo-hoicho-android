package ishopgo.com.exhibition.ui.main.productmanager.detail

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asMoney
import kotlinx.android.synthetic.main.item_product_retated_horizontal.view.*

class ProductManagerDetailRelatedAdapter : ClickableAdapter<Product>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_retated_horizontal
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Product> {
        return Holder(v, ProductConverter())
    }

    inner class Holder(v: View, private val converter: Converter<Product, ProductProvider>) : BaseRecyclerViewAdapter.ViewHolder<Product>(v) {

        override fun populate(data: Product) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                Glide.with(context)
                        .load(convert.provideImage())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                        .into(iv_spk_product_related_image)

                tv_spk_product_related_name.text = convert.provideName()
                tv_spk_product_related_price.text = convert.providePrice()
                btn_spk_product_related_delete.visibility = View.GONE

            }
        }
    }

    interface ProductProvider {
        fun provideImage(): String
        fun provideName(): String
        fun providePrice(): String
        fun provideMarketPrice(): String
    }

    class ProductConverter : Converter<Product, ProductProvider> {

        override fun convert(from: Product): ProductProvider {
            return object : ProductProvider {

                override fun provideImage(): String {
                    return from.image?.trim() ?: ""
                }

                override fun provideName(): String {
                    return from.name?.trim() ?: "unknown"
                }

                override fun providePrice(): String {
                    return from.price.asMoney()
                }

                override fun provideMarketPrice(): String {
                    return from.ttPrice.asMoney()
                }
            }
        }

    }
}