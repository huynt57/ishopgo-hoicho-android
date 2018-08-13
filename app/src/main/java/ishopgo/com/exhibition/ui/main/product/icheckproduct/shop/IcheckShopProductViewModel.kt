package ishopgo.com.exhibition.ui.main.product.icheckproduct.shop

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.IcheckRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.IcheckProduct
import ishopgo.com.exhibition.ui.base.BaseIcheckSingleObserver
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class IcheckShopProductViewModel : BaseListViewModel<List<IcheckProduct>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is IcheckRequest)
            addDisposable(isgService.loadIcheckShopProduct(params.param)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckProduct>>() {
                        override fun success(data: List<IcheckProduct>?) {
                            data?.let { dataReturned.postValue(it) }
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }
}