package ishopgo.com.exhibition.ui.survey

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.survey.Survey
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.model.survey.PostSurvey
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

class SurveyViewModel : BaseApiViewModel(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var survey = MutableLiveData<Survey>()
    var postSusscess = MutableLiveData<Boolean>()


    fun getSurvey() {
        addDisposable(authService.getSurvey()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Survey>() {
                    override fun success(data: Survey?) {
                        survey.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                    }
                }))
    }

    fun postSurvey(surveyId: Long, listResult: MutableList<PostSurvey>) {
        val fields = HashMap<String, Any>()

        for (i in listResult.indices) {
            fields["survey[$i][idQuestion]"] = listResult[i].suvey?.idQuestion.toString()
            fields["survey[$i][idAnswer]"] = listResult[i].suvey?.idAnswer.toString()
            fields["survey[$i][content]"] = listResult[i].suvey?.content.toString()
        }

        addDisposable(authService.postSurvey(surveyId, fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        UserDataManager.currentSurvey = 1
                        postSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }
}