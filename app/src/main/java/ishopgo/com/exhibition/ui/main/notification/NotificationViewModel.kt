package ishopgo.com.exhibition.ui.main.notification

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
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

            val listBooth = mutableListOf<NotificationProvider>()

            for (i in 0..10) {
                listBooth.add(object : NotificationProvider {
                    override fun provideId(): Long {
                        return i.toLong()
                    }

                    override fun provideAccount(): String {
                        return "Nguyễn Huy Hoàng"
                    }

                    override fun provideTitle(): String {
                        return "Thông báo"
                    }

                    override fun provideImage(): String {
                        return "https://static.ishopgo.com/17288/f482d8cb87b9a10094bdefc8deac2283product-1525667159350jpg.jpg"
                    }

                    override fun provideContent(): String {
                        return "Nội dung"
                    }

                    override fun provideShortDescription(): String {
                        return "Nội dung thu gọn"
                    }

                    override fun provideCreatedAt(): String {
                        return "2018/05/07 12:00:00"
                    }

                    override fun provideSender(): String {
                        return "Admin"
                    }

                    override fun provideIsRead(): Int {
                        return if (i % 2 == 0) 0 else 1
                    }

                })
            }
            dataReturned.postValue(listBooth)

//            addDisposable(noAuthService.getCommunity(fields)
//                    .subscribeOn(Schedulers.io())
//                    .subscribeWith(object : BaseSingleObserver<List<Community>>() {
//                        override fun success(data: List<Community>?) {
//                            dataReturned.postValue(data ?: mutableListOf())
//                        }
//
//                        override fun failure(status: Int, message: String) {
//                            resolveError(status, message)
//                        }
//
//
//                    })
//            )
        }
    }

    val TAG = "NotificationViewModel"
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}