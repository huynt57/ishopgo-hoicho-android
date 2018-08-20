package ishopgo.com.exhibition.ui.main.product.detail

import android.annotation.SuppressLint
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Description
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_source_description.view.*

class ListDescriptionAdapter : ClickableAdapter<Description>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_source_description

    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<Description> {
        return Hodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<Description>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class Hodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<Description>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: Description) {
            super.populate(data)

            itemView.apply {
                process_name.text = data.title ?: ""
            }
        }
    }
}