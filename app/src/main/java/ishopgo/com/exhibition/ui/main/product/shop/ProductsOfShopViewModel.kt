package ishopgo.com.exhibition.ui.main.product.shop

import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.RequestParams
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.main.product.ProductProvider

class ProductsOfShopViewModel : BaseListViewModel<List<ProductProvider>>(), AppComponent.Injectable {

    override fun loadData(params: RequestParams) {
        val dummy = mutableListOf<ProductProvider>()
        for (i in 0..5)
            dummy.add(object : IdentityData(), ProductProvider {

                init {
                    id = i.toLong()
                }

                override fun provideImage(): String {
                    return "https://s3-ap-southeast-1.amazonaws.com/ishopgo/1000/ozed-be8f7a057577f05861d0ccfa1ad1dbb921793748fe07e1b870584ab452283e36medi-spotlessjpgjpg.jpg"
                }

                override fun provideName(): String {
                    return "Kem trị thâm mụn Medi Spotless (Đã xem)"
                }

                override fun providePrice(): String {
                    return "520.000 đ"
                }

                override fun provideMarketPrice(): String {
                    return "510.000 đ"
                }

            })

        dataReturned.postValue(dummy)

    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

}
