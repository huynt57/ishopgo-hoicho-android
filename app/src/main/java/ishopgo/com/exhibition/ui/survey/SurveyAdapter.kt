package ishopgo.com.exhibition.ui.survey

import android.annotation.SuppressLint
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.survey.PostAnswer
import ishopgo.com.exhibition.model.survey.PostSurvey
import ishopgo.com.exhibition.model.survey.SurveyQuestion

class SurveyAdapter(private var listQuestion: MutableList<SurveyQuestion>) : PagerAdapter() {
    var listener: CkListener? = null

    companion object {
        val TYPE_OTHER = 2
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return listQuestion.size
    }

    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
                .inflate(R.layout.item_question, container, false)
        val tv_question_number: TextView = view.findViewById(R.id.tv_question_number)
        val tv_question: TextView = view.findViewById(R.id.tv_question)
        val rb_checked: RadioButton = view.findViewById(R.id.rb_checked)
        val rb_checked_2: RadioButton = view.findViewById(R.id.rb_checked_2)
        val rb_checked_3: RadioButton = view.findViewById(R.id.rb_checked_3)
        val rb_checked_4: RadioButton = view.findViewById(R.id.rb_checked_4)
        val edt_answer: TextInputEditText = view.findViewById(R.id.edt_answer)
        val edt_answer_2: TextInputEditText = view.findViewById(R.id.edt_answer_2)
        val edt_answer_3: TextInputEditText = view.findViewById(R.id.edt_answer_3)
        val edt_answer_4: TextInputEditText = view.findViewById(R.id.edt_answer_4)
        val textInputLayout: TextInputLayout = view.findViewById(R.id.textInputLayout)
        val textInputLayout2: TextInputLayout = view.findViewById(R.id.textInputLayout2)
        val textInputLayout3: TextInputLayout = view.findViewById(R.id.textInputLayout3)
        val textInputLayout4: TextInputLayout = view.findViewById(R.id.textInputLayout4)

        tv_question_number.text = "Câu ${position + 1}:"
        tv_question.text = listQuestion[position].title
        rb_checked.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (buttonView.isChecked) {
                    edt_answer.isFocusableInTouchMode = true
                    edt_answer.isFocusable = true
                } else {
                    edt_answer.isFocusableInTouchMode = false
                    edt_answer.isFocusable = false
                }
                val postAnswer = PostAnswer()
                val postSurvey = PostSurvey()
                val listResult = mutableListOf<PostSurvey>()
                postSurvey.suvey = null
                postAnswer.content = edt_answer.text.toString()
                postAnswer.idAnswer = listQuestion[position].answers!![0].id
                postAnswer.idQuestion = listQuestion[position].id
                listQuestion[position].answered = true
                postSurvey.suvey = postAnswer
                listResult.clear()
                listResult.add(postSurvey)
                listener?.clickOption(listResult, position)
            }
        }

        rb_checked_2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (buttonView.isChecked) {
                    edt_answer_2.isFocusableInTouchMode = true
                    edt_answer_2.isFocusable = true
                } else {
                    edt_answer_2.isFocusableInTouchMode = false
                    edt_answer_2.isFocusable = false
                }
                val postAnswer = PostAnswer()
                val postSurvey = PostSurvey()
                val listResult = mutableListOf<PostSurvey>()
                postAnswer.content = edt_answer_2.text.toString()
                postAnswer.idAnswer = listQuestion[position].answers!![1].id
                postAnswer.idQuestion = listQuestion[position].id
                listQuestion[position].answered = true
                postSurvey.suvey = postAnswer
                listResult.clear()
                listResult.add(postSurvey)
                listener?.clickOption(listResult, position)
            }
        }

        rb_checked_3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (buttonView.isChecked) {
                    edt_answer_3.isFocusableInTouchMode = true
                    edt_answer_3.isFocusable = true
                } else {
                    edt_answer_3.isFocusableInTouchMode = false
                    edt_answer_3.isFocusable = false
                }
                val postAnswer = PostAnswer()
                val postSurvey = PostSurvey()
                val listResult = mutableListOf<PostSurvey>()
                postAnswer.content = edt_answer_3.text.toString()
                postAnswer.idAnswer = listQuestion[position].answers!![2].id
                postAnswer.idQuestion = listQuestion[position].id
                listQuestion[position].answered = true
                postSurvey.suvey = postAnswer
                listResult.clear()
                listResult.add(postSurvey)
                listener?.clickOption(listResult, position)
            }
        }


        rb_checked_4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (buttonView.isChecked) {
                    edt_answer_4.isFocusableInTouchMode = true
                    edt_answer_4.isFocusable = true
                } else {
                    edt_answer_4.isFocusableInTouchMode = false
                    edt_answer_4.isFocusable = false
                }
                val postAnswer = PostAnswer()
                val postSurvey = PostSurvey()
                val listResult = mutableListOf<PostSurvey>()
                postAnswer.content = edt_answer_4.text.toString()
                postAnswer.idAnswer = listQuestion[position].answers!![3].id
                postAnswer.idQuestion = listQuestion[position].id
                listQuestion[position].answered = true
                postSurvey.suvey = postAnswer
                listResult.clear()
                listResult.add(postSurvey)
                listener?.clickOption(listResult, position)
            }
        }

        if (listQuestion[position].answers != null && listQuestion[position].answers!!.isNotEmpty())
            for (i in listQuestion[position].answers!!.indices) {
                if (i == 0) {
                    rb_checked.visibility = View.VISIBLE
                    if (listQuestion[position].answers!![i].type == TYPE_OTHER) {
                        rb_checked.text = "Khác"
                        textInputLayout.visibility = View.VISIBLE
                    } else rb_checked.text = listQuestion[position].answers!![i].content
                }
                if (i == 1) {
                    rb_checked_2.visibility = View.VISIBLE
                    if (listQuestion[position].answers!![i].type == TYPE_OTHER) {
                        rb_checked_2.text = "Khác"
                        textInputLayout2.visibility = View.VISIBLE
                    } else rb_checked_2.text = listQuestion[position].answers!![i].content
                }
                if (i == 2) {
                    rb_checked_3.visibility = View.VISIBLE
                    if (listQuestion[position].answers!![i].type == TYPE_OTHER) {
                        rb_checked_3.text = "Khác"
                        textInputLayout3.visibility = View.VISIBLE
                    } else rb_checked_3.text = listQuestion[position].answers!![i].content
                }
                if (i == 3) {
                    rb_checked_4.visibility = View.VISIBLE
                    if (listQuestion[position].answers!![i].type == TYPE_OTHER) {
                        rb_checked_4.text = "Khác"
                        textInputLayout4.visibility = View.VISIBLE
                    } else rb_checked_4.text = listQuestion[position].answers!![i].content
                }
            }

//        adapterAnswer = AnswerAdapter()
//        question.answers?.let { adapterAnswer.replaceAll(it) }
//        rv_answer.layoutManager = LinearLayoutManager(container.context, LinearLayoutManager.VERTICAL, false)
//        rv_answer.adapter = adapterAnswer

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    interface CkListener {
        fun clickOption(listResult: MutableList<PostSurvey>, position: Int)
    }
}