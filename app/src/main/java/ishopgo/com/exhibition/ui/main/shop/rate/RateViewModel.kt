package ishopgo.com.exhibition.ui.main.shop.rate

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.ShopRatesRequest
import ishopgo.com.exhibition.domain.response.ProductComment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentProvider

/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class RateViewModel : BaseListViewModel<List<ProductCommentProvider>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is ShopRatesRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["shop_id"] = params.shopId

            addDisposable(noAuthService.getShopRatings(10, fields)
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : BaseSingleObserver<List<ProductComment>>() {
                        override fun success(data: List<ProductComment>?) {
                            dataReturned.postValue(data ?: listOf())
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