package ishopgo.com.exhibition.ui.main.home.search.product

import android.util.Log
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.RequestParams
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import kotlin.concurrent.thread

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
class SearchProductViewModel : BaseListViewModel<List<SearchProductProvider>>(), AppComponent.Injectable {

    companion object {
        private val TAG = "SearchProductViewModel"
    }

    override fun loadData(params: RequestParams) {
        Log.d(TAG, "loadData: params = [${params}]")
        val dummy = mutableListOf<SearchProductProvider>()
        for (i in 0..19)
            dummy.add(object : IdentityData(), SearchProductProvider {
                override fun provideCode(): String {
                    return "12312412312312"
                }

                init {
                    id = i.toLong()
                }

                override fun provideImage(): String {
                    return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
                }

                override fun provideName(): String {
                    return "Kem trị thâm mụn Medi Spotless"
                }

            })
        thread { Thread.sleep(1000); dataReturned.postValue(dummy) }.start()

    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}