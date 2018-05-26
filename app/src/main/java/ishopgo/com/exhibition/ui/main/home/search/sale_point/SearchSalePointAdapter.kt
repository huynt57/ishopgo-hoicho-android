package ishopgo.com.exhibition.ui.main.home.search.sale_point

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.salepoint.SalePointProvider

class SearchSalePointAdapter(var itemWidthRatio: Float = -1f, var itemHeightRatio: Float = -1F) : ClickableAdapter<SalePointProvider>() {

    var screenWidth: Int = UserDataManager.displayWidth
    var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_search_shop_has_product
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<SalePointProvider> {
        val productHolder = ProductHolder(v)
        val layoutParams = productHolder.itemView.layoutParams

        if (itemWidthRatio > 0)
            layoutParams.width = (screenWidth * itemWidthRatio).toInt()
        if (itemHeightRatio > 0)
            layoutParams.height = (screenHeight * itemHeightRatio).toInt()

        return productHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<SalePointProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    internal inner class ProductHolder(view: View) : BaseRecyclerViewAdapter.ViewHolder<SalePointProvider>(view) {

        override fun populate(data: SalePointProvider) {
            super.populate(data)

            itemView.apply {

            }
        }
    }
}