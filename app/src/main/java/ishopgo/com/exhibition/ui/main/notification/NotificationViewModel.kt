package ishopgo.com.exhibition.ui.main.notification

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.Notification
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by hoangnh on 5/7/2018.
 */
class NotificationViewModel : BaseListViewModel<List<NotificationProvider>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(isgService.getNotifications(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<Notification>>() {
                        override fun success(data: List<Notification>?) {
                            dataReturned.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var marksAllSuccess = MutableLiveData<Boolean>()

    fun marksAllAsRead() {
        val fields = mutableMapOf<String, Any>()
        fields["all"] = 1

        addDisposable(isgService.readNotification(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        marksAllSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var markNotificationSuccess = MutableLiveData<Boolean>()

    fun markAsReadThenShowDetail(notification_Id: Long) {
        val fields = mutableMapOf<String, Any>()
        fields["ids[]"] = notification_Id

        addDisposable(isgService.readNotification(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        markNotificationSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    val TAG = "NotificationViewModel"
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}