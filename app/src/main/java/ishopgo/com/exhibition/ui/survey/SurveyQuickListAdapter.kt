package ishopgo.com.exhibition.ui.survey

import android.annotation.SuppressLint
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.survey.SurveyQuestion
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_question_gridview.view.*

class SurveyQuickListAdapter : ClickableAdapter<SurveyQuestion>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_question_gridview
    }

    override fun createHolder(v: View, viewType: Int): ViewHolder<SurveyQuestion> {
        return Holder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<SurveyQuestion>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.apply {
            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    inner class Holder(v: View) : BaseRecyclerViewAdapter.ViewHolder<SurveyQuestion>(v) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: SurveyQuestion) {
            super.populate(data)

            itemView.apply {
                tv_question_name.text = "Câu ${adapterPosition + 1}"

                if (data.isAnswered()) {
                    tv_question_name.setBackgroundResource(R.color.colorPrimary)
                } else {
                    tv_question_name.setBackgroundResource(R.color.colorPrimaryDark)
                }
            }
        }
    }
}