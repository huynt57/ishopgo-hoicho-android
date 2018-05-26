package ishopgo.com.exhibition.ui.main.home.search.shop

import android.annotation.SuppressLint
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.Shop
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.extensions.asHtml
import kotlinx.android.synthetic.main.item_search_shop_has_product.view.*
import kotlinx.android.synthetic.main.item_search_total.view.*

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
class SearchShopAdapter(var itemWidthRatio: Float = -1f, var itemHeightRatio: Float = -1F) : ClickableAdapter<SearchShopResultProvider>() {
    companion object {
        const val SHOP_TOTAL = 0
        const val SHOP_LIST = 1
    }

    var screenWidth: Int = UserDataManager.displayWidth
    var screenHeight: Int = UserDataManager.displayHeight

    override fun getChildLayoutResource(viewType: Int): Int {
        return if (viewType == SHOP_TOTAL) R.layout.item_search_total else R.layout.item_search_shop_has_product
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == SHOP_TOTAL) SHOP_TOTAL else SHOP_LIST
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<SearchShopResultProvider> {
        return if (viewType == SHOP_TOTAL) {
            TotalHodel(v)
        } else {
            val productHolder = ProductHolder(v)
            val layoutParams = productHolder.itemView.layoutParams

            if (itemWidthRatio > 0)
                layoutParams.width = (screenWidth * itemWidthRatio).toInt()
            if (itemHeightRatio > 0)
                layoutParams.height = (screenHeight * itemHeightRatio).toInt()

            productHolder
        }
    }

    override fun onBindViewHolder(holder: ViewHolder<SearchShopResultProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is TotalHodel) {
            holder.apply {

            }
        } else if (holder is ProductHolder) {
            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }
    }

    inner class TotalHodel(v: View) : BaseRecyclerViewAdapter.ViewHolder<SearchShopResultProvider>(v) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: SearchShopResultProvider) {
            super.populate(data)
            itemView.apply {
                if (data is Shop)
                    tv_total.text = "${data.id} kết quả được tìm thấy"
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