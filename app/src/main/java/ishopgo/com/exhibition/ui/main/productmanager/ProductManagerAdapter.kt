package ishopgo.com.exhibition.ui.main.productmanager

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.product_manager.ProductManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asMoney
import kotlinx.android.synthetic.main.item_product_manager.view.*

/**
 * Created by xuanhong on 2/2/18. HappyCoding!
 */
class ProductManagerAdapter : ClickableAdapter<ProductManager>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ProductManager> {
        return Holder(v, ProductManagerConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<ProductManager>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<ProductManager, ProductManagerProvider>) : BaseRecyclerViewAdapter.ViewHolder<ProductManager>(v) {

        override fun populate(data: ProductManager) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                if (convert.provideStatus() != STATUS_DISPLAY_SHOW && convert.provideStatus() != STATUS_DISPLAY_LANDING_PAGE)
                    img_blur.visibility = View.VISIBLE
                else img_blur.visibility = View.GONE



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
        var STATUS_DISPLAY_SHOW: Int = 2 //Hiển thị dạng chuẩn
        var STATUS_DISPLAY_LANDING_PAGE: Int = 3 //Hiển thị dạng landing page
    }

    interface ProductManagerProvider {
        fun provideName(): String
        fun provideImage(): String
        fun provideCode(): String
        fun providePrice(): String
        fun provideStatus(): Int
    }

    class ProductManagerConverter : Converter<ProductManager, ProductManagerProvider> {
        override fun convert(from: ProductManager): ProductManagerProvider {
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
                        return from.price?.asMoney() ?: "Liên hệ"
                }
            }
        }
    }

}