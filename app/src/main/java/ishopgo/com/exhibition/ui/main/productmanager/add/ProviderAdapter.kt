package ishopgo.com.exhibition.ui.main.productmanager.add

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Provider
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter

class ProviderAdapter : ClickableAdapter<Provider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_region
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<Provider> {
        return RegionHodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<Provider>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class RegionHodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<Provider>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: Provider) {
            super.populate(data)

            val textView = itemView as TextView
            textView.text = data.name ?: ""
        }
    }
}