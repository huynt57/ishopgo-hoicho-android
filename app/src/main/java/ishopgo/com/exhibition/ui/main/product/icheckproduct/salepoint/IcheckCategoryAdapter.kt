package ishopgo.com.exhibition.ui.main.product.icheckproduct.salepoint

import android.annotation.SuppressLint
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.domain.response.IcheckCategory
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_icheck_category.view.*

class IcheckCategoryAdapter : ClickableAdapter<IcheckCategory>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_icheck_category
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<IcheckCategory> {
        return CategoryHodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<IcheckCategory>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class CategoryHodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<IcheckCategory>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: IcheckCategory) {
            super.populate(data)
            itemView?.apply {
                tv_category.text = data.name
            }
        }
    }
}