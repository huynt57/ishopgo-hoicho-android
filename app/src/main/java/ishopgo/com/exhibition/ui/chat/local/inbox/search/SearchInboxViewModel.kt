package ishopgo.com.exhibition.ui.chat.local.inbox.search

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchInboxRequest
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.chat.local.inbox.InboxProvider

/**
 * Created by xuanhong on 5/24/18. HappyCoding!
 */
class SearchInboxViewModel : BaseListViewModel<List<InboxProvider>>(), AppComponent.Injectable {
    override fun loadData(params: Request) {
        if (params is SearchInboxRequest) {
            if (params.keyword.isBlank()) {
                dataReturned.postValue(listOf())
                return
            }

            val dummy = mutableListOf<InboxProvider>()
            for (i in 0..5)
                dummy.add(object : InboxProvider {
                    override fun provideName(): String {
                        return "sample result"
                    }

                    override fun provideAvatar(): String {
                        return "a"
                    }

                    override fun provideMessage(): String {
                        return "sample message"
                    }

                    override fun provideTime(): String {
                        return "10:00"
                    }

                })
            dataReturned.postValue(dummy)
        }
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}