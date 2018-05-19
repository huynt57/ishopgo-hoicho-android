package ishopgo.com.exhibition.ui.chat.local.profile

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.Member
import ishopgo.com.exhibition.domain.response.UserNoteItem
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/6/18. HappyCoding!
 */
class ProfileViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var userData = MutableLiveData<Member>()

    fun loadUserDetail(userId: Long) {
        addDisposable(authService.getMemberDetail(userId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Member>() {
                    override fun success(data: Member?) {
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

    var dateCare = MutableLiveData<String>()

    fun loadDateCare(userId: Long) {
        dateCare.postValue("28/05/2018")
    }

    fun updateDateCare(userId: Long, newDateCare: String) {
        dateCare.postValue(newDateCare)
    }

    var notes = MutableLiveData<List<UserNoteItem>>()

    fun loadNotes(userId: Long) {
        val dummy = mutableListOf<UserNoteItem>()
        for (i in 0..10)
            dummy.add(UserNoteItem("Nội dung ghi chú", "20/10/2017 - 13:20:4${i}"))

        notes.postValue(dummy.take(5))
    }


}