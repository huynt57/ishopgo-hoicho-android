package ishopgo.com.exhibition.ui.chat.local.contact

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.ContactItem
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class ContactViewModel : BaseListViewModel<List<ContactProvider>>(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = HashMap<String, Any>()
            fields["offset"] = params.offset
            fields["limit"] = params.limit

            addDisposable(isgService.inbox_getContact(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<ContactItem>>() {
                        override fun success(data: List<ContactItem>?) {
                            val dummy = mutableListOf<ContactProvider>()
                            for (i in 0..5)
                                dummy.add(object : ContactProvider {
                                    override fun provideAvatar(): String {
                                        return ""
                                    }

                                    override fun provideName(): String {
                                        return "sample"
                                    }

                                    override fun providePhone(): String {
                                        return "0974427143"
                                    }

                                })

                            val list = mutableListOf<ContactProvider>()
                            data?.let { list.addAll(it) }
                            list.addAll(dummy)

                            dataReturned.postValue(list)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    }))

        }
    }


}