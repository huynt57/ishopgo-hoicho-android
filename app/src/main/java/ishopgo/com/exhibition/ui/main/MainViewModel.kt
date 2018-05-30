package ishopgo.com.exhibition.ui.main

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.response.LocalConversationItem
import ishopgo.com.exhibition.domain.response.PusherChatMessage
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.main.home.category.CategoryProvider

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
class MainViewModel : BaseApiViewModel(), AppComponent.Injectable {

    var isSearchEnable = MutableLiveData<Boolean>()
    var enableSearchInbox = MutableLiveData<Boolean>()
    var enableSearchContact = MutableLiveData<Boolean>()
    var currentConversation = MutableLiveData<LocalConversationItem>()
    var newMessage = MutableLiveData<PusherChatMessage>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun enableSearch() {
        isSearchEnable.postValue(true)
    }

    var showCategoriedProducts = MutableLiveData<CategoryProvider>()

    fun showCategoriedProducts(category: CategoryProvider) {
        showCategoriedProducts.postValue(category)
    }

    fun enableSearchInbox() {
        enableSearchInbox.postValue(true)
    }

    fun enableSearchContact() {
        enableSearchContact.postValue(true)
    }

    fun openCurrentConversation(conv: LocalConversationItem) {
        currentConversation.value = conv
    }

    fun closeCurrentConversation() {
        currentConversation.value = null
    }

    /**
     * simply forward this message
     */
    fun resolveMessage(message: PusherChatMessage): Boolean {
        newMessage.postValue(message)
        return true
    }

}