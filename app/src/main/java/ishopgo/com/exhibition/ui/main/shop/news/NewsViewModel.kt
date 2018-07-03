package ishopgo.com.exhibition.ui.main.shop.news

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.BoothPostRequest
import ishopgo.com.exhibition.domain.request.PostCategoryRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.model.post.BoothPostManager
import ishopgo.com.exhibition.model.post.PostCategory
import ishopgo.com.exhibition.model.post.PostObject
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel

class NewsViewModel : BaseListViewModel<List<PostObject>>(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun loadData(params: Request) {
        if (params is BoothPostRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["booth_id"] = params.booth_id
            fields["category_id"] = params.category_id

            addDisposable(noAuthService.getBoothPost(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<BoothPostManager>() {
                        override fun success(data: BoothPostManager?) {
                            dataReturned.postValue(data?.posts ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }


                    })
            )
        }
    }

    var getCategorySusscess = MutableLiveData<List<PostCategory>>()

    fun loadCategory(params: Request) {
        if (params is PostCategoryRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = 999
            fields["offset"] = params.offset
            fields["type"] = params.type

            addDisposable(noAuthService.getPostCategory(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<List<PostCategory>>() {
                        override fun success(data: List<PostCategory>?) {
                            getCategorySusscess.postValue(data)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }
}