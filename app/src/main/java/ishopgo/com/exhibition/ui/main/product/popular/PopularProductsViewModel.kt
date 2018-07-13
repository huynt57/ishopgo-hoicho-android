package ishopgo.com.exhibition.ui.main.product.popular

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchByNameRequest
import ishopgo.com.exhibition.domain.response.FilterProductRequest
import ishopgo.com.exhibition.domain.response.Product
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class PopularProductsViewModel : BaseListViewModel<List<Product>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()

            fields["limit"] = params.limit
            fields["offset"] = params.offset

            if (params is SearchByNameRequest) {
                if (!params.name.isNullOrEmpty()) {
                    fields["name"] = params.name!!
                }
            }

            if (params is FilterProductRequest) {
                fields["sort_by"] = params.sort_by
                fields["sort_type"] = params.sort_type

                if (params.type_filter.isNotEmpty()) {
                    val listType = params.type_filter
                    for (i in listType.indices)
                        fields["type_filter[$i]"] = listType[i]

                }
            }

            addDisposable(noAuthService.getHighlightProducts(fields)
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
