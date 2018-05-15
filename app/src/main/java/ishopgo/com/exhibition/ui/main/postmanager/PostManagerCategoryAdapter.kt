package ishopgo.com.exhibition.ui.main.postmanager

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.post.PostCategory
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter

class PostManagerCategoryAdapter  : ClickableAdapter<PostCategory>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_region
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<PostCategory> {
        return RegionHodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<PostCategory>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class RegionHodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<PostCategory>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: PostCategory) {
            super.populate(data)

            val textView = itemView as TextView
            textView.text = data.name
        }
    }
}