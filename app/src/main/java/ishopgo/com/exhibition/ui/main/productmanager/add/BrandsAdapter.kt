package ishopgo.com.exhibition.ui.main.productmanager.add

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Brand
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter

class BrandsAdapter : ClickableAdapter<Brand>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_region
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<Brand> {
        return RegionHodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<Brand>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class RegionHodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<Brand>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: Brand) {
            super.populate(data)

            val textView = itemView as TextView
            textView.text = data.name ?: ""
        }
    }
}