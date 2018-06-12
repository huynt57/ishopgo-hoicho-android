package ishopgo.com.exhibition.ui.main.map

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 6/11/18. HappyCoding!
 */
class ExpoShopViewModel : BaseListViewModel<List<ExpoShopProvider>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        val list = mutableListOf<ExpoShopProvider>()
        for (i in 0..10) {
            list.add(object : ExpoShopProvider {
                override fun provideName(): CharSequence {
                    return if (i == 1 || i == 3)
                        "Chưa có gian hàng"
                    else
                        "Medi White $i"
                }

                override fun provideNumber(): CharSequence {
                    return i.toString()
                }

                override fun provideRegion(): CharSequence {
                    return if (i == 1 || i == 3)
                        ""
                    else
                        "Daclak"
                }

            })
        }

        dataReturned.postValue(list)
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}