package ishopgo.com.exhibition.ui.survey

import android.annotation.SuppressLint
import android.view.View
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.survey.SurveyAnswer
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.base.widget.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_answer.view.*

class AnswerAdapter : ClickableAdapter<SurveyAnswer>() {

    override fun getChildLayoutResource(viewType: Int): Int {
        return R.layout.item_answer
    }

    override fun createHolder(v: View, viewType: Int): BaseRecyclerViewAdapter.ViewHolder<SurveyAnswer> {
        return RegionHodel(v)
    }

    override fun onBindViewHolder(holder: ViewHolder<SurveyAnswer>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.apply {
            itemView.rb_checked.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.text == "Khác") {
                    itemView.edt_answer.isFocusable = true
                    itemView.edt_answer.isFocusableInTouchMode = true
                }
                listener?.click(adapterPosition, getItem(adapterPosition))
            }
//            itemView.setOnClickListener { listener?.click(adapterPosition, getItem(adapterPosition)) }
        }
    }

    internal inner class RegionHodel(itemView: View) : BaseRecyclerViewAdapter.ViewHolder<SurveyAnswer>(itemView) {

        @SuppressLint("SetTextI18n")
        override fun populate(data: SurveyAnswer) {
            super.populate(data)
            itemView.apply {
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
}