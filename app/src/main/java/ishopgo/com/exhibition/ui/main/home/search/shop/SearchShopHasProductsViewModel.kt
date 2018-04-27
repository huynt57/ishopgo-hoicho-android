package ishopgo.com.exhibition.ui.main.home.search.shop

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.RequestParams
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import kotlin.concurrent.thread

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class SearchShopHasProductsViewModel : BaseListViewModel<List<SearchShopResultProvider>>(), AppComponent.Injectable {

    override fun loadData(params: RequestParams) {
        val dummy = mutableListOf<SearchShopResultProvider>()
        for (i in 0..19)
            dummy.add(object : IdentityData(), SearchShopResultProvider {
                override fun provideProductCount(): String {
                    return "10"
                }

                override fun provideJoinedDate(): String {
                    return "28/05/2017"
                }

                init {
                    id = i.toLong()
                }

                override fun provideName(): String {
                    return "Công ty TNHH Mua Đồ Tốt"
                }

            })

        thread { Thread.sleep(1000); dataReturned.postValue(dummy) }.start()

    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}