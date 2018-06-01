package ishopgo.com.exhibition.ui.survey

import android.annotation.SuppressLint
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.survey.PostAnswer
import ishopgo.com.exhibition.model.survey.PostSurvey
import ishopgo.com.exhibition.model.survey.SurveyAnswer
import ishopgo.com.exhibition.model.survey.SurveyQuestion

class SurveyAdapter(private var listQuestion: MutableList<SurveyQuestion>) : PagerAdapter() {
    var listener: CkListener? = null

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
        val rv_answer: RecyclerView = view.findViewById(R.id.rv_answer)

        tv_question_number.text = "Câu ${position + 1}:"
        tv_question.text = listQuestion[position].title

        val adapterAnswer = AnswerAdapter()
        listQuestion[position].answers?.let { adapterAnswer.replaceAll(it) }
        rv_answer.layoutManager = LinearLayoutManager(container.context, LinearLayoutManager.VERTICAL, false)
        rv_answer.adapter = adapterAnswer
        adapterAnswer.answerListener = object : AnswerAdapter.AnswerClickListener {
            override fun onAnswerSelected(i: Int, data: SurveyAnswer, content: String) {
                val postAnswer = PostAnswer()
                val postSurvey = PostSurvey()
                val listResult = mutableListOf<PostSurvey>()
                postAnswer.content = content
                postAnswer.idAnswer = listQuestion[position].answers!![i].id
                postAnswer.idQuestion = listQuestion[position].id
                listQuestion[position].answered = true
                postSurvey.suvey = postAnswer
                listResult.clear()
                listResult.add(postSurvey)
                listener?.clickOption(listResult, position)
            }
        }

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