package ishopgo.com.exhibition.ui.chat.local.contact.search

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchContactRequest
import ishopgo.com.exhibition.domain.response.ContactItem
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.chat.local.contact.ContactProvider

/**
 * Created by xuanhong on 5/24/18. HappyCoding!
 */
class SearchContactViewModel : BaseListViewModel<List<ContactProvider>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is SearchContactRequest) {
            if (params.keyword.isBlank()) {
                dataReturned.postValue(listOf())
                return
            }

            val fields = HashMap<String, Any>()
            fields["offset"] = params.offset
            fields["limit"] = params.limit
            fields["name"] = params.keyword

            addDisposable(isgService.inbox_getContact(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<ContactItem>>() {
                        override fun success(data: List<ContactItem>?) {
                            dataReturned.postValue(data ?: listOf())
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