package ishopgo.com.exhibition.ui.main.home.search.shop

import android.support.v7.widget.RecyclerView
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.asHtml
import kotlinx.android.synthetic.main.item_search_shop_has_product.view.*

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class SearchShopAdapter(var itemWidthRatio: Float = -1f, var itemHeightRatio: Float = -1F) : ClickableAdapter<SearchShopResultProvider>() {

    var screenWidth: Int = UserDataManager.displayWidth
    var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_search_shop_has_product
    }


    override fun createHolder(v: View, viewType: Int): ViewHolder<SearchShopResultProvider> {
            val productHolder = ProductHolder(v)
            val layoutParams = productHolder.itemView.layoutParams

            if (itemWidthRatio > 0)
                layoutParams.width = (screenWidth * itemWidthRatio).toInt()
            if (itemHeightRatio > 0)
                layoutParams.height = (screenHeight * itemHeightRatio).toInt()

        return productHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<SearchShopResultProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ProductHolder) {
            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }
    }

    internal inner class ProductHolder(view: View) : BaseRecyclerViewAdapter.ViewHolder<SearchShopResultProvider>(view) {

        override fun populate(data: SearchShopResultProvider) {
            super.populate(data)

            itemView.apply {
                view_name.text = data.provideName()
                view_product_count.text = "<b><font color=\"#009624\">${data.provideProductCount()}</font></b> sản phẩm".asHtml()
                view_joined_date.text = "Tham gia <b><font color=\"#009624\">${data.provideJoinedDate()}</font></b>".asHtml()
            }
        }
    }
}