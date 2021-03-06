package ishopgo.com.exhibition.ui.main.stamp.stampmanager

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.StampManager
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class StampManagerViewModel : BaseListViewModel<List<StampManager>>(), AppComponent.Injectable {

    var total = MutableLiveData<Long>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(authService.loadStampManager(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<StampManager>>() {
                        override fun success(data: List<StampManager>?) {
                            dataReturned.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }
}