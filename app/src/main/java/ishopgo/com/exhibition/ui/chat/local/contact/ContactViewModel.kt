package ishopgo.com.exhibition.ui.chat.local.contact

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.CreateConversationRequest
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.ContactItem
import ishopgo.com.exhibition.domain.response.NewConversation
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
class ContactViewModel : BaseListViewModel<List<ContactProvider>>(), AppComponent.Injectable {

    var conversation = MutableLiveData<NewConversation>()

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
                            dataReturned.postValue(data ?: listOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    }))

        }
    }

    fun createConversation(params: Request) {
        if (params is CreateConversationRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["type"] = params.type
            params.member.mapIndexed { index, memId ->
                fields["member[$index]"] = memId
            }

            addDisposable(isgService.inbox_createNewChat(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<NewConversation>() {
                        override fun success(data: NewConversation?) {
                            conversation.postValue(data)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    }))

        }
    }


}