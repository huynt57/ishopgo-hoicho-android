package ishopgo.com.exhibition.ui.main.shop.info

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.home.search.sale_point.SearchSalePointProvider
import kotlinx.android.synthetic.main.item_product_sale_point.view.*

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class SalePointAdapter : ClickableAdapter<SearchSalePointProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_sale_point
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<SearchSalePointProvider> {
        return SalePointHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<SearchSalePointProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            holder.itemView.setOnClickListener {
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }
    }

    inner class SalePointHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<SearchSalePointProvider>(v) {

        override fun populate(data: SearchSalePointProvider) {
            super.populate(data)

            itemView.apply {
                tv_product_sale_point_name.text = data.provideName()
                tv_product_sale_point_address.text = data.provideAddress()
                tv_product_sale_point_price.text = data.provideCountProduct()
                tv_product_sale_point_phone.text = data.providePhone()
            }

        }
    }
}