package ishopgo.com.exhibition.ui.login

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter

/**
 * Created by hoangnh on 4/24/2018.
 */
class RegionAdapter : BaseRecyclerViewAdapter<Region>() {
    var listenerClick: onClickListener? = null

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_region
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<Region> {
        return PlanHodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<Region>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listenerClick?.onClick(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class PlanHodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<Region>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: Region) {
            super.populate(data)

            val textView = itemView as TextView
            textView.text = data.name
        }
    }

    interface onClickListener {
        fun onClick(position: Int, item: Region)
    }
}