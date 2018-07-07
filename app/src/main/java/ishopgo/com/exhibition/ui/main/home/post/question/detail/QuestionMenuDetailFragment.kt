package ishopgo.com.exhibition.ui.main.postmanager.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.question.QuestionDetail
import ishopgo.com.exhibition.ui.base.BaseFragment
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.main.home.post.question.QuestionMenuViewModel
import ishopgo.com.exhibition.ui.widget.VectorSupportTextView
import kotlinx.android.synthetic.main.fragment_question_detail.*

class QuestionMenuDetailFragment : BaseFragment() {
    private var questionId: Long = -1L
    private lateinit var viewModel: QuestionMenuViewModel
    private var questionDetail: QuestionDetail? = null

    companion object {
        const val TAG = "QuestionMenuDetailFragment"
        fun newInstance(params: Bundle): QuestionMenuDetailFragment {
            val fragment = QuestionMenuDetailFragment()
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
        viewModel = obtainViewModel(QuestionMenuViewModel::class.java, false)
        viewModel.errorSignal.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                resolveError(it)
            }
        })

        viewModel.getContentSusscess.observe(this, Observer { p ->
            p?.let {
                questionDetail = it
                val converted = QuestDetailConverter().convert(it)
                tv_question_title.text = converted.provideTitle()
                tv_question_time.text = converted.provideTime()
                tv_question_answer.text = converted.provideAnswer()
            }
        })

        viewModel.getPostContent(questionId)
    }

    fun sharePost() {
        context?.let {
            val dialog = MaterialDialog.Builder(it)
                    .customView(R.layout.dialog_community_share, false)
                    .autoDismiss(false)
                    .canceledOnTouchOutside(true)
                    .build()
            val tv_share_facebook = dialog.findViewById(R.id.tv_share_facebook) as VectorSupportTextView
            tv_share_facebook.setOnClickListener {
                questionDetail?.let { it1 -> shareFacebook(it1) }
            }
            val tv_share_zalo = dialog.findViewById(R.id.tv_share_zalo) as VectorSupportTextView
            tv_share_zalo.setOnClickListener {
                questionDetail?.let { it1 -> shareApp(it1) }
            }
            dialog.show()
        }
    }

    private var callbackManager: CallbackManager? = null

    private fun shareFacebook(questionDetail: QuestionDetail) {
        callbackManager = CallbackManager.Factory.create()
        val shareDialog = ShareDialog(this)

        shareDialog.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
            override fun onSuccess(result: Sharer.Result?) {
            }

            override fun onCancel() {
                toast("Chia sẻ bị huỷ bỏ")
            }

            override fun onError(error: FacebookException?) {
                toast(error.toString())
            }
        })

        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
//            val urlToShare = postContent.linkAffiliate.toString()
            val urlToShare = "http://hangviet360.com/hoi-dap"
            val shareContent = ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(urlToShare))
                    .build()

            shareDialog.show(shareContent)
        }
    }

    private fun shareApp(questionDetail: QuestionDetail) {
        val urlToShare = "http://hangviet360.com/hoi-dap"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, urlToShare)
        startActivity(shareIntent)
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