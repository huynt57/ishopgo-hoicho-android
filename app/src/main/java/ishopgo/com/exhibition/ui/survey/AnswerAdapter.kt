package ishopgo.com.exhibition.ui.survey

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.survey.SurveyAnswer
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_answer.view.*
import kotlin.math.log

class AnswerAdapter : ClickableAdapter<SurveyAnswer>() {
    var answerListener: AnswerClickListener? = null

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_answer
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<SurveyAnswer> {
        return RegionHodel(v)
    }

    private var mSelectedItem = -1

    override fun onBindViewHolder(holder: ViewHolder<SurveyAnswer>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.rb_checked.setOnCheckedChangeListener { buttonView, isChecked ->
                mSelectedItem = adapterPosition

                if (buttonView.text == "Khác") {
                    itemView.edt_answer.isFocusable = true
                    itemView.edt_answer.isFocusableInTouchMode = true
                }
                if (isChecked)
                    answerListener?.onAnswerSelected(adapterPosition, getItem(adapterPosition), itemView.edt_answer.text.toString())
                buttonView.post { notifyDataSetChanged() }
            }
        }
    }

    internal inner class RegionHodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<SurveyAnswer>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: SurveyAnswer) {
            super.populate(data)
            itemView.apply {
                itemView.rb_checked.isChecked = adapterPosition == mSelectedItem

                tv_stt.text = adapterPosition.toString()

                if (data.type == TYPE_OTHER) {
                    rb_checked.text = "Khác"
                    textInputLayout.visibility = View.VISIBLE
                } else
                    rb_checked.text = data.content
            }
        }
    }

    companion object {
        val TYPE_OTHER = 2
    }

    interface AnswerClickListener {
        fun onAnswerSelected(position: Int, data: SurveyAnswer, content: String)
    }
}