package ishopgo.com.exhibition.ui.main.shop.category

import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.BoothCategoriesRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.Category
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
class CategoryViewModel : BaseListViewModel<List<Category>>(), AppComponent.Injectable {

    override fun loadData(params: Request) {
        if (params is BoothCategoriesRequest) {
            addDisposable(noAuthService.getBoothCategories(params.boothId)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<Category>>() {
                        override fun success(data: List<Category>?) {
                            data?.map { it.level = 1 }
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