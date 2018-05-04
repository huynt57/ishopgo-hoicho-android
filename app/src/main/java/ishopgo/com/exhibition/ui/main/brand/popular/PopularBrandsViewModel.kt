package ishopgo.com.exhibition.ui.main.brand.popular

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.Brand
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider

class PopularBrandsViewModel : BaseListViewModel<List<HighlightBrandProvider>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        addDisposable(noAuthService.getHighlightBrands()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<List<Brand>>() {
                    override fun success(data: List<Brand>?) {
                        dataReturned.postValue(data ?: mutableListOf())
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
