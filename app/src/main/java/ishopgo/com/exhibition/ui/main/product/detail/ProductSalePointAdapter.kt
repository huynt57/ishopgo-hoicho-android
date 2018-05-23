package ishopgo.com.exhibition.ui.main.product.detail

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_product_sale_point.view.*

class ProductSalePointAdapter : ClickableAdapter<ProductSalePointProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_product_sale_point
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<ProductSalePointProvider> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<ProductSalePointProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<ProductSalePointProvider>(v) {

        override fun populate(data: ProductSalePointProvider) {
            super.populate(data)

            itemView.apply {
                tv_product_sale_point_address.text = data.provideAddress()
                tv_product_sale_point_name.text = data.provideName()
                tv_product_sale_point_phone.text = data.providePhone()
                tv_product_sale_point_price.text = data.providePrice()
            }
        }

    }
}