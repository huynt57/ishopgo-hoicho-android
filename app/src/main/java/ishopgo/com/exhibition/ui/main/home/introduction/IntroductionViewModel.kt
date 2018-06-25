package ishopgo.com.exhibition.ui.main.home.introduction

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.Introduction
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 6/23/18. HappyCoding!
 */
class IntroductionViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var introduction = MutableLiveData<String>()

    fun getIntroduction() {
        addDisposable(noAuthService.getIntroduction()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Introduction>() {
                    override fun success(data: Introduction?) {
                        introduction.postValue(data?.content)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )
    }
}