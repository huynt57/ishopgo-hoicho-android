package ishopgo.com.exhibition.ui.main.home.search.sale_point

import android.annotation.SuppressLint
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_search_sale_point.view.*
import kotlinx.android.synthetic.main.item_search_total.view.*

class SearchSalePointAdapter(var itemWidthRatio: Float = -1f, var itemHeightRatio: Float = -1F) : ClickableAdapter<SearchSalePointProvider>() {

    companion object {
        const val SALE_POINT_TOTAL = 0
        const val SALE_POINT_LIST = 1
    }

    var screenWidth: Int = UserDataManager.displayWidth
    var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return if (viewType == SALE_POINT_TOTAL) R.layout.item_search_total else R.layout.item_search_sale_point
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == SALE_POINT_TOTAL) SALE_POINT_TOTAL else SALE_POINT_LIST
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<SearchSalePointProvider> {
        return if (viewType == SALE_POINT_TOTAL) {
            TotalHodel(v)
        } else {
            val salePointHolder = SalePointHolder(v)
            val layoutParams = salePointHolder.itemView.layoutParams

            if (itemWidthRatio > 0)
                layoutParams.width = (screenWidth * itemWidthRatio).toInt()
            if (itemHeightRatio > 0)
                layoutParams.height = (screenHeight * itemHeightRatio).toInt()

            return salePointHolder
        }
    }

    override fun onBindViewHolder(holder: ViewHolder<SearchSalePointProvider>, position: Int) {
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

    inner class TotalHodel(v: View) : BaseRecyclerViewAdapter.ViewHolder<SearchSalePointProvider>(v) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: SearchSalePointProvider) {
            super.populate(data)
            itemView.apply {
                if (data is SearchSalePoint)
                    tv_total.text = "${data.id} kết quả được tìm thấy"
            }
        }
    }

    internal inner class SalePointHolder(view: View) : BaseRecyclerViewAdapter.ViewHolder<SearchSalePointProvider>(view) {

        override fun populate(data: SearchSalePointProvider) {
            super.populate(data)

            itemView.apply {
                view_name.text = data.provideName()
                view_address.text = data.provideAddress()
                view_product_count.text = data.provideCountProduct()
                view_phone.text = data.providePhone()
            }
        }
    }
}