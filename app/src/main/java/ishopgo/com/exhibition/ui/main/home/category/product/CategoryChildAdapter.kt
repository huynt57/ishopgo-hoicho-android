package ishopgo.com.exhibition.ui.main.home.category.product

import android.graphics.Color
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider
import kotlinx.android.synthetic.main.item_category_child_only_text.view.*

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class CategoryChildAdapter : ClickableAdapter<CategoryProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_category_child_only_text
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<CategoryProvider> {
        return ChildHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<CategoryProvider>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            listener?.click(adapterPosition, getItem(adapterPosition))
        }
    }

    inner class ChildHolder(v: View) : BaseRecyclerViewAdapter.ViewHolder<CategoryProvider>(v) {

        override fun populate(data: CategoryProvider) {
            super.populate(data)

            itemView.apply {
                view_name.text = data.provideName()
                setBackgroundColor(Color.parseColor(Const.colors[adapterPosition % Const.colors.size]))
            }
        }

    }


}