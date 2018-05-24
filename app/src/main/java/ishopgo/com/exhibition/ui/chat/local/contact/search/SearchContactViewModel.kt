package ishopgo.com.exhibition.ui.chat.local.contact.search

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchContactRequest
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

            val dummy = mutableListOf<ContactProvider>()
            for (i in 0..5)
                dummy.add(object : ContactProvider {
                    override fun providePhone(): String {
                        return "0984472141"
                    }

                    override fun provideName(): String {
                        return "sample result"
                    }

                    override fun provideAvatar(): String {
                        return "a"
                    }

                })
            dataReturned.postValue(dummy)
        }
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}