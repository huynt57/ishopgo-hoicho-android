package ishopgo.com.exhibition.ui.main.administrator

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.request.LoadMoreRequest
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.model.administrator.Administrator
import ishopgo.com.exhibition.model.administrator.AdministratorPermissions
import ishopgo.com.exhibition.model.administrator.AdministratorRole
import ishopgo.com.exhibition.ui.base.list.BaseListViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

class AdministratorViewModel : BaseListViewModel<List<Administrator>>(), AppComponent.Injectable {
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    @SuppressLint("StaticFieldLeak")
    @Inject
    lateinit var appContext: Application

    override fun loadData(params: Request) {
        if (params is LoadMoreRequest) {
            val fields = mutableMapOf<String, Any>()
            fields["limit"] = params.limit
            fields["offset"] = params.offset

            addDisposable(authService.getAdministrator(fields)
                    .subscribeOn(Schedulers.single())
                    .subscribeWith(object : BaseSingleObserver<MutableList<Administrator>>() {
                        override fun success(data: MutableList<Administrator>?) {
                            dataReturned.postValue(data ?: mutableListOf())
                        }

                        override fun failure(status: Int, message: String) {
                            resolveError(status, message)
                        }
                    })
            )
        }
    }

    var deleteSusscess = MutableLiveData<Boolean>()

    fun deleteAdministrator(accountId: Long) {

        addDisposable(authService.deleteAdministrator(accountId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        deleteSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var dataAdministratorPermissions = MutableLiveData<MutableList<AdministratorPermissions>>()

    fun getAdministratorPermissions() {

        addDisposable(authService.getAdministratorPermissions()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<MutableList<AdministratorPermissions>>() {
                    override fun success(data: MutableList<AdministratorPermissions>?) {
                        dataAdministratorPermissions.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    var createSusscess = MutableLiveData<Boolean>()

    fun addAdministrator(permissions: ArrayList<AdministratorRole>, phone: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)

        if (permissions.isNotEmpty())
            for (i in permissions.indices)
                builder.addFormDataPart("permissions[]", permissions[i].key ?: "")

        addDisposable(authService.addAdministrator(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        createSusscess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }
}