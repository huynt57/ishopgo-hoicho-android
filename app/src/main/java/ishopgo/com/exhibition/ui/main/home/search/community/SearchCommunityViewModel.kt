package ishopgo.com.exhibition.ui.main.home.search.community

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchCommunityRequest
import ishopgo.com.exhibition.model.community.ManagerCommunity
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.community.CommunityProvider

class SearchCommunityViewModel : BaseListViewModel<List<CommunityProvider>>(), AppComponent.Injectable {

    companion object {
        private val TAG = "SearchCommunityViewModel"
    }

    var total = MutableLiveData<Int>()

    override fun loadData(params: Request) {
        if (params is SearchCommunityRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["last_id"] = params.last_id
            fields["content"] = params.content

            addDisposable(noAuthService.getCommunity(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManagerCommunity>() {
                        override fun success(data: ManagerCommunity?) {
                            total.postValue(data?.total ?: 0)
                            dataReturned.postValue(data?.post ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}