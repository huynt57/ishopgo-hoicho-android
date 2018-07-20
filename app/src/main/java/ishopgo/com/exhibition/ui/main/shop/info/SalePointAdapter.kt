package ishopgo.com.exhibition.ui.main.shop.info

import android.content.Intent
import android.net.Uri
import android.text.Spanned
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asStylePhoneNumber
import kotlinx.android.synthetic.main.item_product_sale_point.view.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class SalePointAdapter : ClickableAdapter<SearchSalePoint>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_sale_point
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<SearchSalePoint> {
        return SalePointHolder(v, SearchSalePointConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<SearchSalePoint>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            holder.itemView.setOnClickListener {
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }
    }

    inner class SalePointHolder(v: View, private val converter: Converter<SearchSalePoint, SearchSalePointProvider>) : BaseRecyclerViewAdapter.ViewHolder<SearchSalePoint>(v) {

        override fun populate(data: SearchSalePoint) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_product_sale_point_name.text = convert.provideName()
                tv_product_sale_point_address.text = convert.provideAddress()
                tv_product_sale_point_price.text = convert.provideCountProduct()
                tv_product_sale_point_phone.text = convert.providePhone().asHtml()
                tv_product_sale_point_phone.setOnClickListener {
                    val uri = Uri.parse("tel:${convert.providePhone()}")
                    val i = Intent(Intent.ACTION_DIAL, uri)
                    it.context.startActivity(i)
                }
            }

        }
    }

    interface SearchSalePointProvider {
        fun provideAddress(): Spanned
        fun provideName(): Spanned
        fun providePhone(): String
        fun provideCountProduct(): String
    }

    class SearchSalePointConverter : Converter<SearchSalePoint, SearchSalePointProvider> {

        override fun convert(from: SearchSalePoint): SearchSalePointProvider {
            return object : SearchSalePointProvider {
                override fun provideAddress(): Spanned {
                    return "${from.address?.trim() ?: ""}, ${from.district?.trim()
                            ?: ""}, ${from.city?.trim() ?: ""}.".asHtml()
                }

                override fun provideName(): Spanned {
                    return "<b>${from.name ?: ""}</b>".asHtml()
                }

                override fun providePhone(): String {
                    return from.phone?.asStylePhoneNumber() ?: ""
                }

                override fun provideCountProduct(): String {
                    return "${from.countProduct ?: 0} sản phẩm"
                }
            }
        }

    }
}