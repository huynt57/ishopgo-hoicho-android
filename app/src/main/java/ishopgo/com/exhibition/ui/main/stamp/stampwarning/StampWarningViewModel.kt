package ishopgo.com.exhibition.ui.main.stamp.stampwarning

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.StampListWarning
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import okhttp3.MultipartBody

class StampWarningViewModel : BaseListViewModel<List<StampListWarning>>(), AppComponent.Injectable {

    var total = MutableLiveData<Long>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(authService.loadStampWarning(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<StampListWarning>>() {
                        override fun success(data: List<StampListWarning>?) {
                            dataReturned.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var evictionSuccess = MutableLiveData<Boolean>()

    fun evictionStampWarning(stampId: Long, code: String, productId: Long, note: String) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("product_id", productId.toString())
                .addFormDataPart("stamp_id", stampId.toString())
                .addFormDataPart("code", code)
                .addFormDataPart("note", note)

        addDisposable(authService.evictionStampWarning(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        evictionSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var restoreSuccess = MutableLiveData<Boolean>()

    fun restoreStampWarning(code: String, note: String) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("note", note)

        addDisposable(authService.restoreStampWarning(code, builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        restoreSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }
}