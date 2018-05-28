package ishopgo.com.exhibition.ui.chat.local.inbox

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class InboxViewModel : BaseListViewModel<List<InboxProvider>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(isgService.inbox_getConversations(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<LocalConversationItem>>() {
                        override fun success(data: List<LocalConversationItem>?) {
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