package ishopgo.com.exhibition.ui.main.questmanager

import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.question.QuestionObject
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import ishopgo.com.exhibition.ui.base.widget.Converter
import kotlinx.android.synthetic.main.item_qa.view.*

class QuestionManagerAdapter : ClickableAdapter<QuestionObject>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_qa
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<QuestionObject> {
        return Holder(v, QuestionConverter())
    }

    override fun onBindViewHolder(holder: ViewHolder<QuestionObject>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View, private val converter: Converter<QuestionObject, QuestProvider>) : BaseRecyclerViewAdapter.ViewHolder<QuestionObject>(v) {

        override fun populate(data: QuestionObject) {
            super.populate(data)

            val convert = converter.convert(data)
            itemView.apply {
                tv_qa_title.text = convert.provideTitle()
                tv_qa_info.text = convert.provideTime()
            }
        }

    }
}