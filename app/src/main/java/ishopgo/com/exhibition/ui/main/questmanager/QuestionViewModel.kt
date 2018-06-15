package ishopgo.com.exhibition.ui.main.questmanager

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.QuestionRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.model.question.QuestionCategory
import ishopgo.com.exhibition.model.question.QuestionDetail
import ishopgo.com.exhibition.model.question.QuestionManager
import ishopgo.com.exhibition.model.question.QuestionObject
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import javax.inject.Inject

class QuestionViewModel : BaseListViewModel<List<QuestionObject>>(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    @SuppressLint("StaticFieldLeak")
    @Inject
    lateinit var appContext: Application

    override fun loadData(params: Request) {
        if (params is QuestionRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["key_search"] = params.key_search
            fields["category_id"] = params.category_id
            fields["status"] = params.status

            addDisposable(isgService.getQuestion(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<QuestionManager>() {
                        override fun success(data: QuestionManager?) {
                            dataReturned.postValue(data?.objects ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var getContentSusscess = MutableLiveData<QuestionDetail>()

    fun getPostContent(post_id: Long) {
        addDisposable(isgService.getQuestionDetail(post_id)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<QuestionDetail>() {
                    override fun success(data: QuestionDetail?) {
                        getContentSusscess.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var getCategorySusscess = MutableLiveData<List<QuestionCategory>>()

    fun loadCategory(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(isgService.getQuestionCategory(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<QuestionCategory>>() {
                        override fun success(data: List<QuestionCategory>?) {
                            getCategorySusscess.postValue(data)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }
}