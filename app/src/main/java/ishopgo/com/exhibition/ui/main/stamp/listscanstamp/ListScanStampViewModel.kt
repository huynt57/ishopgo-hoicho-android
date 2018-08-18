package ishopgo.com.exhibition.ui.main.stamp.listscanstamp

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.StampDistribution
import ishopgo.com.exhibition.domain.response.StampManager
import ishopgo.com.exhibition.domain.response.StampUserListScan
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class ListScanStampViewModel : BaseListViewModel<List<StampUserListScan>>(), AppComponent.Injectable {

    var total = MutableLiveData<Long>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(authService.loadListScanStamp(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<StampUserListScan>>() {
                        override fun success(data: List<StampUserListScan>?) {
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