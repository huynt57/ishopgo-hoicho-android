package ishopgo.com.exhibition.ui.main.home.search.sale_point

import android.annotation.SuppressLint
import android.text.Spanned
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.extensions.asPhone
import kotlinx.android.synthetic.main.item_product_sale_point.view.*
import kotlinx.android.synthetic.main.item_search_total.view.*

class SearchSalePointAdapter(var itemWidthRatio: Float = -1f, var itemHeightRatio: Float = -1F) : ClickableAdapter<SearchSalePoint>() {

    companion object {
        const val SALE_POINT_TOTAL = 0
        const val SALE_POINT_LIST = 1
    }

    var screenWidth: Int = UserDataManager.displayWidth
    var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return if (viewType == SALE_POINT_TOTAL) R.layout.item_search_total else R.layout.item_product_sale_point
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == SALE_POINT_TOTAL) SALE_POINT_TOTAL else SALE_POINT_LIST
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<SearchSalePoint> {
        return if (viewType == SALE_POINT_TOTAL) {
            TotalHodel(v)
        } else {
            val salePointHolder = SalePointHolder(v, SearchSalePointConverter())
            val layoutParams = salePointHolder.itemView.layoutParams

            if (itemWidthRatio > 0)
                layoutParams.width = (screenWidth * itemWidthRatio).toInt()
            if (itemHeightRatio > 0)
                layoutParams.height = (screenHeight * itemHeightRatio).toInt()

            return salePointHolder
        }
    }

    override fun onBindViewHolder(holder: ViewHolder<SearchSalePoint>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is TotalHodel) {
            holder.apply {

            }
        } else if (holder is SalePointHolder) {
            holder.apply {
                holder.itemView.setOnClickListener {
                    listener?.click(adapterPosition, getItem(adapterPosition))
                }
            }
        }
    }

    inner class TotalHodel(v: View) : BaseRecyclerViewAdapter.ViewHolder<SearchSalePoint>(v) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: SearchSalePoint) {
            super.populate(data)
            itemView.apply {
                    tv_total.text = "${data.id} kết quả được tìm thấy"
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
                tv_product_sale_point_phone.text = convert.providePhone()
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
                    return "${from.address ?: ""}, ${from.district ?: ""}, ${from.city
                            ?: ""}".asHtml()
                }

                override fun provideName(): Spanned {
                    return "<b>${from.name ?: ""}</b>".asHtml()
                }

                override fun providePhone(): String {
                    return from.phone?.asPhone() ?: ""
                }

                override fun provideCountProduct(): String {
                    return "${from.countProduct ?: 0} sản phẩm"
                }
            }
        }

    }
}