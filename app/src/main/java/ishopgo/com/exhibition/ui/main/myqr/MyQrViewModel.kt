package ishopgo.com.exhibition.ui.main.myqr

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.BoothConfig
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 5/21/18. HappyCoding!
 */
class MyQrViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var booth = MutableLiveData<BoothConfig>()

    fun loadQrCode() {
        addDisposable(authService.getConfigBooth()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<BoothConfig>() {
                    override fun success(data: BoothConfig?) {
                        data?.let { booth.postValue(it) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )

    }

}