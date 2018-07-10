package ishopgo.com.exhibition.ui.main.product.newest

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.FilterProductRequest
import ishopgo.com.exhibition.domain.response.NewestProducts
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class NewestProductsViewModel : BaseListViewModel<List<Product>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is FilterProductRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["sort_by"] = params.sort_by
            fields["sort_type"] = params.sort_type

            if (params.type_filter.isNotEmpty()) {
                val listType = params.type_filter
                for (i in listType.indices)
                    fields["type_filter[$i]"] = listType[i]

            }

            addDisposable(noAuthService.getNewestProducts(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<NewestProducts>() {
                        override fun success(data: NewestProducts?) {
                            dataReturned.postValue(data?.data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }

                    })
            )
        } else if (params is LoadMoreRequest)
            addDisposable(noAuthService.getNewestProducts(params.toMap())
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<NewestProducts>() {
                        override fun success(data: NewestProducts?) {
                            dataReturned.postValue(data?.data ?: mutableListOf())
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
