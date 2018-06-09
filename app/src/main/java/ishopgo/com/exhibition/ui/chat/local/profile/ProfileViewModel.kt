package ishopgo.com.exhibition.ui.chat.local.profile

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.CreateConversationRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.domain.response.NewConversation
import ishopgo.com.exhibition.model.Profile
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

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

    var conversation = MutableLiveData<NewConversation>()

    fun createConversation(params: Request) {
        if (params is CreateConversationRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["type"] = params.type
            params.member.mapIndexed { index, memId ->
                fields["member[$index]"] = memId
            }

            addDisposable(isgService.inbox_createNewChat(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<NewConversation>() {
                        override fun success(data: NewConversation?) {
                            conversation.postValue(data)
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    }))

        }
    }

}