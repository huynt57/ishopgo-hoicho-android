package ishopgo.com.exhibition.ui.main.product.promotion

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchByNameRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.domain.response.SearchProducts
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class PromotionProductsViewModel : BaseListViewModel<List<Product>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = params.toMap()

            if (params is SearchByNameRequest) {
                params.name?.let {
                    fields["name"] = it
                }
            }

            addDisposable(noAuthService.getPromotionProducts(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<Product>>() {
                        override fun success(data: List<Product>?) {
                            dataReturned.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }

                    })
            )
        }
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }
}
