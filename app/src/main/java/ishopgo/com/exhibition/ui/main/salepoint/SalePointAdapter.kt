package ishopgo.com.exhibition.ui.main.salepoint

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_list_sale_point.view.*

class SalePointAdapter : ClickableAdapter<SalePointProvider>() {
    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_list_sale_point
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<SalePointProvider> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<SalePointProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.btn_status.setOnClickListener {
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<SalePointProvider>(v) {

        override fun populate(data: SalePointProvider) {
            super.populate(data)

            itemView.apply {
                tv_sale_point_product_name.text = data.provideProductName()
                tv_sale_point_product_phone.text = data.providePhone()
                tv_sale_point_name.text = data.provideName()
                tv_sale_point_address.text = data.provideAddress()
                tv_sale_point_district.text = data.provideDistrict()
                tv_sale_point_city.text = data.provideCity()
                btn_status.isChecked = data.provideWasStatus()
                btn_status.text = data.provideStatus()
            }
        }

    }

    companion object {
        val STATUS_SHOW = 1
    }
}