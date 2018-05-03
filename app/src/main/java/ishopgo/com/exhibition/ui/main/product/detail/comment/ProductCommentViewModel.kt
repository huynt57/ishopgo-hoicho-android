package ishopgo.com.exhibition.ui.main.product.detail.comment

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.ProductCommentsRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.ProductComment
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class ProductCommentViewModel : BaseListViewModel<List<ProductCommentProvider>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is ProductCommentsRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            if (params.lastId != -1L) fields["last_id"] = params.lastId
            if (params.parentId != -1L) fields["parent_id"] = params.parentId

            addDisposable(noAuthService.getProductComments(params.productId, fields)
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