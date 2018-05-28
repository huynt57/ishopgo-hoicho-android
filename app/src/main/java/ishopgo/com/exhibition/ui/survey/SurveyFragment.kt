package ishopgo.com.exhibition.ui.survey

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.survey.SurveyAnswer
import ishopgo.com.exhibition.model.survey.SurveyQuestion
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.survey.PostAnswer
import ishopgo.com.exhibition.model.survey.PostSurvey
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_survey.*

class SurveyFragment : BaseFragment() {
    companion object {
        fun newInstance(params: Bundle): SurveyFragment {
            val fragment = SurveyFragment()
            fragment.arguments = params

            return fragment
        }
    }

    private lateinit var viewModel: SurveyViewModel
    private lateinit var adapterSurvey: SurveyAdapter
    private var adapterQuestionQuick = SurveyQuickListAdapter()
    private var listQuestion = mutableListOf<SurveyQuestion>()
    private var listAnswer = mutableListOf<PostSurvey>()
    private var position = 0
    private var surveyId: Long = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_survey, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_user_name.text = "Tên khách hàng: <b>${UserDataManager.currentUserName}</b>".asHtml()
        tv_user_phone.text = "Số điện thoại: <b>${UserDataManager.currentUserPhone}</b>".asHtml()

        btn_start.setOnClickListener {
            showProgressDialog()
            viewModel.getSurvey()
        }

        imb_question_quick.setOnClickListener {
            if (layout_question_quick.visibility == View.GONE) {
                layout_question_quick.visibility = View.VISIBLE
                viewpager.visibility = View.GONE
            } else {
                layout_question_quick.visibility = View.GONE
                viewpager.visibility = View.VISIBLE
            }
        }
        setupRecycleViewQuestionQuick()
    }

    private fun setupRecycleViewQuestionQuick() {
        rv_question_quick.layoutManager = GridLayoutManager(context,5)
        rv_question_quick.adapter = adapterQuestionQuick
        adapterQuestionQuick.listener = object : ClickableAdapter.BaseAdapterAction<SurveyQuestion> {
            override fun click(position: Int, data: SurveyQuestion, code: Int) {
                layout_question_quick.visibility = View.GONE
                viewpager.visibility = View.VISIBLE
                viewpager.setCurrentItem(position, false)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = obtainViewModel(SurveyViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer { error ->
            error?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })
        viewModel.survey.observe(this, Observer { p ->
            p?.let {
                layout_infor.visibility = View.GONE
                layout_content.visibility = View.VISIBLE
                hideProgressDialog()
                listQuestion = it.questions ?: mutableListOf()
                adapterSurvey = SurveyAdapter(listQuestion)
                adapterQuestionQuick.replaceAll(listQuestion)
                surveyId = it.id
                viewpager.adapter = adapterSurvey
                viewpager.offscreenPageLimit = it.questions?.size ?: 0
                viewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(i: Int, positionOffset: Float, positionOffsetPixels: Int) {
                        position = i
                    }

                    override fun onPageSelected(position: Int) {

                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })
                imb_preview.setOnClickListener {
                    position--
                    if (position < 0) {
                        position = 0
                    }
                    viewpager.setCurrentItem(position, false)
                }
                imb_next.setOnClickListener {
                    position++
                    if (position > listQuestion.size - 1) {
                        position = listQuestion.size - 1
                    }
                    viewpager.setCurrentItem(position, false)
                }

                adapterSurvey.listener = object : SurveyAdapter.CkListener {
                    override fun clickOption(listResult: MutableList<PostSurvey>, position: Int) {
                        if (position < listAnswer.size) {
                            listAnswer.removeAt(position)
                        }
                        listAnswer.add(position, listResult[0])
                        adapterQuestionQuick.replaceAll(listQuestion)
                    }
                }
                imb_end.setOnClickListener {
                    if (listAnswer.isNotEmpty())
                        viewModel.postSurvey(surveyId, listAnswer)
                    else toast("Bạn vui lòng trả lời câu hỏi khảo sát")
                }
            }
        })
        viewModel.postSusscess.observe(this, Observer {
            toast("Gửi đáp án khảo sát thành công")
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        })
    }
}