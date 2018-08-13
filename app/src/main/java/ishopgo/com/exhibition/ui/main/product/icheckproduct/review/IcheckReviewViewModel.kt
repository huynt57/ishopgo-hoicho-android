package ishopgo.com.exhibition.ui.main.product.icheckproduct.review

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.IcheckRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.IcheckReview
import ishopgo.com.exhibition.ui.base.BaseIcheckSingleObserver
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class IcheckReviewViewModel : BaseListViewModel<List<IcheckReview>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is IcheckRequest)
            addDisposable(isgService.getIcheckReview(params.param)
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : BaseIcheckSingleObserver<List<IcheckReview>>() {
                        override fun success(data: List<IcheckReview>?) {
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