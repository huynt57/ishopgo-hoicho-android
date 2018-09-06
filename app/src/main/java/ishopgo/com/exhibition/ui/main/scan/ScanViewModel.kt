package ishopgo.com.exhibition.ui.main.scan

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.BaseResponse
import ishopgo.com.exhibition.domain.response.IcheckProduct
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.base.BaseIcheckSingleObserver

/**
 * Created by xuanhong on 4/25/18. HappyCoding!
 */
class ScanViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var scanIcheckResult = MutableLiveData<IcheckProduct>()
    var totalNoResult = MutableLiveData<String>()

    fun loadIcheckProduct(code: String) {
        addDisposable(isgService.getIcheckProduct(String.format("%s/%s", "https://ishopgo.icheck.com.vn/scan", code))
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseIcheckSingleObserver<IcheckProduct>() {
                    override fun success(data: IcheckProduct?) {
                        if (data != null) {
                            val isClone = data.isClone ?: false
                            if (isClone)
                                totalNoResult.postValue(code)
                            else
                                scanIcheckResult.postValue(data)

                        } else
                            totalNoResult.postValue(code)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )

    }

    var linkQRCode = MutableLiveData<String>()

    fun getLinkQRCode(url: String) {
        addDisposable(noAuthService.getStampLinkScan(url)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<String>() {
                    override fun success(data: String?) {
                        linkQRCode.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        linkQRCode.postValue(url)
                    }
                }))
    }
}