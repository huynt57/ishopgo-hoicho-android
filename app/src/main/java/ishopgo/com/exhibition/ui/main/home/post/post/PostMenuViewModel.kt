package ishopgo.com.exhibition.ui.main.home.post.post

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.PostCategoryRequest
import ishopgo.com.exhibition.domain.request.PostRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.model.post.PostCategory
import ishopgo.com.exhibition.model.post.PostContent
import ishopgo.com.exhibition.model.post.PostsManager
import ishopgo.com.exhibition.model.postmenu.PostMenuManager
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import ishopgo.com.exhibition.ui.main.postmanager.PostProvider
import javax.inject.Inject

class PostMenuViewModel : BaseListViewModel<List<PostProvider>>(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    @SuppressLint("StaticFieldLeak")
    @Inject
    lateinit var appContext: Application
    var total = MutableLiveData<Int>()

    override fun loadData(params: Request) {
        if (params is PostRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset
            fields["name"] = params.name
            fields["type"] = params.type
            fields["category_id"] = params.category_id

            addDisposable(noAuthService.getPost(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<PostMenuManager>() {
                        override fun success(data: PostMenuManager?) {
                            total.postValue(data?.totalPost ?: 0)
                            dataReturned.postValue(data?.posts ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var getContentSusscess = MutableLiveData<PostContent>()

    fun getPostContent(post_id: Long) {
        addDisposable(noAuthService.getPostDetail(post_id)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<PostContent>() {
                    override fun success(data: PostContent?) {
                        getContentSusscess.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var getCategorySusscess = MutableLiveData<List<PostCategory>>()

    fun loadCategory(params: Request) {
        if (params is PostCategoryRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
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