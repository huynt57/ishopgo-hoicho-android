package ishopgo.com.exhibition.ui.main.home.search.member

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.MemberRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.model.member.ManageMember
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.main.membermanager.MemberManagerProvider

class SearchMemberViewModel : BaseListViewModel<List<MemberManagerProvider>>(), AppComponent.Injectable {

    companion object {
        private val TAG = "SearchProductViewModel"
    }

    override fun loadData(params: Request) {
        if (params is MemberRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["name"] = params.name

            addDisposable(isgService.getMember(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManageMember>() {
                        override fun success(data: ManageMember?) {
                            dataReturned.postValue(data?.member ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    override fun inject(appComponent: AppComponent) {
//        appComponent.inject(this)
    }

}