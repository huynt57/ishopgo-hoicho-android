package ishopgo.com.exhibition.ui.main.stamp.nostamp.add

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter

class TitleAdapter : ClickableAdapter<String>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_region
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<String> {
        return Hodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<String>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class Hodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<String>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: String) {
            super.populate(data)

            val textView = itemView as TextView
            textView.text = data
        }
    }
}