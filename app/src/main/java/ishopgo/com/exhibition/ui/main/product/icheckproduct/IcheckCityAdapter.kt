package ishopgo.com.exhibition.ui.main.product.icheckproduct

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IcheckCity
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter

class IcheckCityAdapter : ClickableAdapter<IcheckCity.City>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_region
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<IcheckCity.City> {
        return RegionHodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<IcheckCity.City>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class RegionHodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<IcheckCity.City>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: IcheckCity.City) {
            super.populate(data)

            val textView = itemView as TextView
            textView.text = data.cityName
        }
    }
}