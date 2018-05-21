package ishopgo.com.exhibition.ui.main.myqr

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.Booth
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 5/21/18. HappyCoding!
 */
class MyQrViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var qrImage = MutableLiveData<String>()

    fun loadQrCode() {
        addDisposable(authService.getConfigBooth()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Booth>() {
                    override fun success(data: Booth?) {
                        data?.let { qrImage.postValue(it.qrcode) }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )

    }

}