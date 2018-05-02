package ishopgo.com.exhibition.ui.main.account

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.BaseDataResponse
import ishopgo.com.exhibition.model.AccountMenuItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.login.LoginViewModel
import okhttp3.MultipartBody

class AccountViewModel : BaseApiViewModel(), AppComponent.Injectable {
    val TAG = "AccountViewModel"
    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var menu = MutableLiveData<List<AccountMenuProvider>>()
    var changePassword = MutableLiveData<Boolean>()
    var getOTP = MutableLiveData<Boolean>()

    fun loadMenu() {
        val items = mutableListOf<AccountMenuProvider>()
        items.add(AccountMenuItem(
                Const.AccountAction.ACTION_PROFILE,
                R.drawable.ic_menu_profile,
                "Thông tin cá nhân"
        ))
        items.add(AccountMenuItem(
                Const.AccountAction.ACTION_CHANGE_PASSWORD,
                R.drawable.ic_menu_change_password,
                "Đổi mật khẩu"
        ))
        items.add(AccountMenuItem(
                Const.AccountAction.ACTION_LOGOUT,
                R.drawable.ic_menu_logout,
                "Đăng xuất",
                textColor = R.color.md_red_500
        ))

        menu.postValue(items)

    }

    var loggedOut = MutableLiveData<Boolean>()

    fun logout() {
        addDisposable(apiService.logout("android")
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : DisposableSingleObserver<Any>() {
                    override fun onError(e: Throwable?) {
                        Log.d(TAG, e.toString())
                    }

                    override fun onSuccess(t: Any?) {
                        loggedOut.postValue(true)
                    }
                })
        )
    }

    fun changePassword(phone: String, otp: String, password: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)
                .addFormDataPart("otp", otp)
                .addFormDataPart("password", password)
                .addFormDataPart("id_app", LoginViewModel.id_app)

        addDisposable(apiService.changePassword(builder.build())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : BaseSingleObserver<BaseDataResponse>() {
                    override fun success(data: BaseDataResponse?) {
                        changePassword.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    fun getOTP(phone: String) {
        val fields = mutableMapOf<String, Any>()
        fields.put("id_app", LoginViewModel.id_app)
        fields.put("phone", phone)

        addDisposable(apiService.getOTP(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<BaseDataResponse>() {
                    override fun success(data: BaseDataResponse?) {
                        getOTP.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                }))
    }

}
