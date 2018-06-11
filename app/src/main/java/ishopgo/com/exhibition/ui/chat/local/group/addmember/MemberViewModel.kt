package ishopgo.com.exhibition.ui.chat.local.group.addmember

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.ContactItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 1/19/18. HappyCoding!
 */
class MemberViewModel : BaseApiViewModel(), AppComponent.Injectable {
    companion object {
        private val TAG = "ContactViewModel"
    }

    var contacts: MutableLiveData<List<IMemberView>> = MutableLiveData()
    var addOK: MutableLiveData<Boolean> = MutableLiveData()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun loadContacts(offset: Int, searchKeyword: String = "") {
        val fields = HashMap<String, Any>()
        fields.put("offset", offset)
        fields.put("limit", Const.PAGE_LIMIT)
        if (searchKeyword.isNotBlank())
            fields.put("name", searchKeyword)

        addDisposable(isgService.inbox_getContact(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<ContactItem>>() {
                    override fun success(data: List<ContactItem>?) {
                        contacts.postValue(data ?: mutableListOf())
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))


    }

    fun addMember(conversationId: String, selectedMembers: List<IMemberView>) {
        val fields = mutableMapOf<String, Any>()
        fields["conver"] = conversationId
        selectedMembers.mapIndexed { index, iMemberView ->
            if (iMemberView is ContactItem)
                fields.put("member[$index]", iMemberView.id)
        }

        addDisposable(isgService.inbox_addMemberGroup(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        addOK.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))


    }

}