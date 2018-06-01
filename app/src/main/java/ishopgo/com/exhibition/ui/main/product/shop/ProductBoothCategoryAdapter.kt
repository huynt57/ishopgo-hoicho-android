package ishopgo.com.exhibition.ui.main.product.shop

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider

class ProductBoothCategoryAdapter : ClickableAdapter<CategoryProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_region
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<CategoryProvider> {
        return RegionHodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<CategoryProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class RegionHodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<CategoryProvider>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: CategoryProvider) {
            super.populate(data)

            val textView = itemView as TextView
            if (data.provideName() == "Tất cả danh mục") {
                textView.text = data.provideName()
            } else
                textView.text = "${data.provideName()} (${data.provideCount()})"
        }
    }
}