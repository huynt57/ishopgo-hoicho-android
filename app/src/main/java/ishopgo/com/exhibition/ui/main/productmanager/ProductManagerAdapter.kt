package ishopgo.com.exhibition.ui.main.productmanager

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.model.product_manager.ProductManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asMoney
import kotlinx.android.synthetic.main.item_product_manager.view.*

/**
 * Created by xuanhong on 2/2/18. HappyCoding!
 */
class ProductManagerAdapter : ClickableAdapter<Product>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<Product> {
        return Holder(v, ProductManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<Product>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is Holder) {
            holder.apply {
                itemView.iv_options.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_OPTION) }
                itemView.iv_push_top.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition), CLICK_PUSH_TOP) }
            }

        }

    }

    inner class Holder(v: View, private val converter: Converter<Product, ProductManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<Product>(v) {

        override fun populate(data: Product) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                if (convert.provideStatus() != STATUS_DISPLAY_SHOW && convert.provideStatus() != STATUS_DISPLAY_LANDING_PAGE)
                    iv_display.visibility = View.VISIBLE
                else iv_display.visibility = View.GONE



                Glide.with(context)
                        .load(convert.provideImage())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                        .into(iv_thumb)

                view_name.text = convert.provideName()
                tv_price.text = convert.providePrice()
                view_code.text = convert.provideCode()
            }
        }

    }

    companion object {
        const val STATUS_DISPLAY_SHOW: Int = 2 //Hiển thị dạng chuẩn
        const val STATUS_DISPLAY_LANDING_PAGE: Int = 3 //Hiển thị dạng landing page
        const val CLICK_OPTION = 1
        const val CLICK_PUSH_TOP = 2
    }

    interface ProductManagerProvider {
        fun provideName(): String
        fun provideImage(): String
        fun provideCode(): String
        fun providePrice(): String
        fun provideStatus(): Int
    }

    class ProductManagerConverter : Converter<Product, ProductManagerProvider> {
        override fun convert(from: Product): ProductManagerProvider {
            return object : ProductManagerProvider {
                override fun provideStatus(): Int {
                    return from.status ?: 0
                }

                override fun provideName(): String {
                    return from.name ?: ""
                }

                override fun provideImage(): String {
                    return from.image ?: ""
                }

                override fun provideCode(): String {
                    return "MSP: ${from.code}"
                }

                override fun providePrice(): String {
                    return if (from.price == 0L) "Liên hệ"
                    else
                        return from.price?.asMoney()
                }
            }
        }
    }

}