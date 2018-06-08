package ishopgo.com.exhibition.ui.chat.local.profile

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.request.SearchCommunityRequest
import ishopgo.com.exhibition.model.Profile
import ishopgo.com.exhibition.model.community.Community
import ishopgo.com.exhibition.model.community.ManagerCommunity
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.community.CommunityProvider

/**
 * Created by xuanhong on 4/6/18. HappyCoding!
 */
class ProfileViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var userData = MutableLiveData<UserInfoProvider>()

    fun loadUserDetail(userId: Long) {
        addDisposable(authService.getProfile(userId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Profile>() {
                    override fun success(data: Profile?) {
                        data?.let {
                            userData.postValue(it)
                        }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }


                })
        )
    }

    var dataCommunity = MutableLiveData<List<CommunityProvider>>()

    fun loadProfileCommunity(params: Request) {
        if (params is SearchCommunityRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["last_id"] = params.last_id
            fields["account_id"] = params.account_id

            addDisposable(noAuthService.getCommunity(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<ManagerCommunity>() {
                        override fun success(data: ManagerCommunity?) {
                            dataCommunity.postValue(data?.post ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var deleteSusscess = MutableLiveData<Boolean>()

    fun deleteMember(member_Id: Long) {

        addDisposable(isgService.deleteMember(member_Id)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        deleteSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var postLikeSuccess = MutableLiveData<Any>()

    fun postCommunityLike(post_id: Long) {
        addDisposable(authService.postCommunityLike(post_id)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        postLikeSuccess.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }
}