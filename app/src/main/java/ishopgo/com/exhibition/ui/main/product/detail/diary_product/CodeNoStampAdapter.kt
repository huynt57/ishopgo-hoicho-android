package ishopgo.com.exhibition.ui.main.product.detail.diary_product

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.CodeNoStamp
import ishopgo.com.exhibition.model.BoothManager
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter

class CodeNoStampAdapter : ClickableAdapter<CodeNoStamp>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_region
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<CodeNoStamp> {
        return RegionHodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<CodeNoStamp>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class RegionHodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<CodeNoStamp>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: CodeNoStamp) {
            super.populate(data)

            val textView = itemView as TextView
            textView.text = data.code ?: ""
        }
    }
}