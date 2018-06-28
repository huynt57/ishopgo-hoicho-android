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
                Glide.with(context)
                        .load(convert.provideImage())
                        .apply(RequestOptions.placeholderOf(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                        .into(iv_thumb)

                tv_item_name.text = convert.provideName()
                tv_price.text = convert.providePrice()
                tv_item_code.text = convert.provideCode()
                tv_department.text = convert.provideDepartment()
            }
        }

    }

    interface ProductManagerProvider {
        fun provideName(): String
        fun provideImage(): String
        fun provideCode(): String
        fun provideTTPrice(): String
        fun providePrice(): String
        fun provideDepartment(): String
    }

    class ProductManagerConverter : Converter<ProductManager, ProductManagerProvider> {
        override fun convert(from: ProductManager): ProductManagerProvider {
            return object : ProductManagerProvider {

                override fun provideDepartment(): String {
                    return from.department ?: ""
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

                override fun provideTTPrice(): String {
                    return from.ttPrice?.asMoney() ?: "0 đ"
                }

                override fun providePrice(): String {
                    return from.price?.asMoney() ?: "0 đ"
                }
            }
        }
    }

}