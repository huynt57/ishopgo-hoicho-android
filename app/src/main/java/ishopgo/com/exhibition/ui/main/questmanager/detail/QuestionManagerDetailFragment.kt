package ishopgo.com.exhibition.ui.main.questmanager.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.question.QuestionDetail
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.main.questmanager.QuestionViewModel
import kotlinx.android.synthetic.main.fragment_question_detail.*

class QuestionManagerDetailFragment : BaseFragment() {
    private var questionId: Long = -1L
    private lateinit var viewModel: QuestionViewModel

    companion object {
        fun newInstance(params: Bundle): QuestionManagerDetailFragment {
            val fragment = QuestionManagerDetailFragment()
            fragment.arguments = params

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_question_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionId = arguments?.getLong(Const.TransferKey.EXTRA_ID) ?: -1L
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(QuestionViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.getContentSusscess.observe(this, Observer { p ->
            p?.let {
                val converter = QuestDetailConverter().convert(it)
                tv_question_title.text = converter.provideTitle()
                tv_question_time.text = converter.provideTime()
                tv_question_answer.text = converter.provideAnswer()
            }
        })

        viewModel.getPostContent(questionId)
    }

    interface QuestDetailProvider {
        fun provideTitle(): String
        fun provideTime(): String
        fun provideAnswer(): String
    }

    class QuestDetailConverter : Converter<QuestionDetail, QuestDetailProvider> {

        override fun convert(from: QuestionDetail): QuestDetailProvider {
            return object : QuestDetailProvider {
                override fun provideTitle(): String {
                    return from.title ?: ""
                }

                override fun provideTime(): String {
                    return from.createdAt?.asDateTime() ?: ""
                }

                override fun provideAnswer(): String {
                    return "Câu trả lời: ${from.answer?.asHtml()}"
                }


            }
        }
    }
}