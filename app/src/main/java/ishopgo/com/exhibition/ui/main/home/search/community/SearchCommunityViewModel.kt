package ishopgo.com.exhibition.ui.main.home.search.community

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchCommunityRequest
import ishopgo.com.exhibition.model.community.Community
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.community.CommunityProvider

class SearchCommunityViewModel : BaseListViewModel<List<CommunityProvider>>(), AppComponent.Injectable {

    companion object {
        private val TAG = "SearchCommunityViewModel"
    }

    override fun loadData(params: Request) {
        if (params is SearchCommunityRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["last_id"] = params.last_id
            fields["content"] = params.content

            addDisposable(noAuthService.getCommunity(fields)
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : BaseSingleObserver<List<Community>>() {
                        override fun success(data: List<Community>?) {
                            dataReturned.postValue(data ?: mutableListOf())
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