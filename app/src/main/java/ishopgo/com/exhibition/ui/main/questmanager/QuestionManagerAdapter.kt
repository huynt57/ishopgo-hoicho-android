package ishopgo.com.exhibition.ui.main.questmanager

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_new_manager.view.*

class QuestionManagerAdapter : ClickableAdapter<QuestProvider>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_new_manager
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<QuestProvider> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<QuestProvider>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<QuestProvider>(v) {

        override fun populate(data: QuestProvider) {
            super.populate(data)

            itemView.apply {
                tv_news_title.text = data.provideTitle()
                tv_news_time.text = data.provideTime()
                tv_news_category.text = data.provideCategoryName()
            }
        }

    }
}