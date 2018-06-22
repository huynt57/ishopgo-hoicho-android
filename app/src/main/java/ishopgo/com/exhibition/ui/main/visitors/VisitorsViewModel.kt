package ishopgo.com.exhibition.ui.main.visitors

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.model.ManageVisitor
import ishopgo.com.exhibition.model.Visitor
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class VisitorsViewModel : BaseListViewModel<List<Visitor>>(), AppComponent.Injectable {

    var total = MutableLiveData<Long>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(authService.getVisitors(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManageVisitor>() {
                        override fun success(data: ManageVisitor?) {
                            data?.let {
                                total.postValue(it.total)
                            }
                            dataReturned.postValue(data?.visitors ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }
}