package ishopgo.com.exhibition.ui.main.home.search.sale_point

import android.text.Spanned
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.setPhone
import kotlinx.android.synthetic.main.item_product_sale_point.view.*

class SearchSalePointAdapter(var itemWidthRatio: Float = -1f, var itemHeightRatio: Float = -1F) : ClickableAdapter<SearchSalePoint>() {

    var screenWidth: Int = UserDataManager.displayWidth
    var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_sale_point
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<SearchSalePoint> {
        val salePointHolder = SalePointHolder(v, SearchSalePointConverter())
        val layoutParams = salePointHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()

        return salePointHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<SearchSalePoint>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is SalePointHolder) {
            holder.apply {
                holder.itemView.setOnClickListener {
                    listener?.click(adapterPosition, getItem(adapterPosition))
                }
            }
        }
    }

    internal inner class SalePointHolder(view: View, private val converter: Converter<SearchSalePoint, SearchSalePointProvider>) : BaseRecyclerViewAdapter.ViewHolder<SearchSalePoint>(view) {

        override fun populate(data: SearchSalePoint) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_product_sale_point_name.text = convert.provideName()
                tv_product_sale_point_address.text = convert.provideAddress()
                tv_product_sale_point_price.text = convert.provideCountProduct()
                tv_product_sale_point_price.setPhone(convert.providePhone(), data.phone ?: "")
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
                    return from.phone ?: ""
                }

                override fun provideCountProduct(): String {
                    return "${from.countProduct ?: 0} sản phẩm"
                }
            }
        }

    }
}