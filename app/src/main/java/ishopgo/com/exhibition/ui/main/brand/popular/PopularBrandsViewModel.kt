package ishopgo.com.exhibition.ui.main.brand.popular

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.RequestParams
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider

class PopularBrandsViewModel : BaseListViewModel<List<HighlightBrandProvider>>(), AppComponent.Injectable {

    override fun loadData(params: RequestParams) {
        val dummy = mutableListOf<HighlightBrandProvider>()
        for (i in 0..5)
            dummy.add(object : IdentityData(), HighlightBrandProvider {

                init {
                    id = i.toLong()
                }

                override fun provideImage(): String {
                    return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/11793/d2deb19a06df9d842fb60a966f1d2b9amy-pham-cao-cap-tphcm-napie-skinpng.png"
                }

            })

        dataReturned.postValue(dummy)

    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }
}
