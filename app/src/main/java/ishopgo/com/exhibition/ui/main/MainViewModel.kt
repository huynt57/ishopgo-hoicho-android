package ishopgo.com.exhibition.ui.main

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.Category
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

    /**
     * simply forward this message
     */
    fun resolveMessage(message: PusherChatMessage): Boolean {
        newMessage.postValue(message)
        return true
    }

    var notificationCount = MutableLiveData<Int>()

    fun loadUnreadNotificationCount() {
        addDisposable(isgService.getNotificationCount()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Int>() {
                    override fun success(data: Int?) {
                        notificationCount.postValue(data ?: 0)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )

    }

    var inboxUnreadCount = MutableLiveData<Int>()

    fun loadUnreadInboxCount() {
        addDisposable(isgService.chatUnreadInbox()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Int>() {
                    override fun success(data: Int?) {
                        inboxUnreadCount.postValue(data ?: 0)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                })
        )

    }

    var openSearchInCategory = MutableLiveData<Category>()

    fun searchInCategory(category: Category) {
        openSearchInCategory.postValue(category)
    }
}