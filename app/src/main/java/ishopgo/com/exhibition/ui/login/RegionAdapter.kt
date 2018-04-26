package ishopgo.com.exhibition.ui.login

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter

/**
 * Created by hoangnh on 4/24/2018.
 */
class RegionAdapter : ClickableAdapter<Region>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_region
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<Region> {
        return RegionHodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<Region>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class RegionHodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<Region>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: Region) {
            super.populate(data)

            val textView = itemView as TextView
            textView.text = data.name
        }
    }
}