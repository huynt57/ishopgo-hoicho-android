package ishopgo.com.exhibition.ui.main.account

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.AccountMenuItem
import ishopgo.com.exhibition.model.Const
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
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

        if (UserDataManager.currentType == "Chủ gian hàng") {
            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_SETTING_BOTTH,
                    R.drawable.ic_finger,
                    "Cấu hình gian hàng"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Khách hàng tham quan"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý sản phẩm"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOTIFICATION,
                    R.drawable.ic_finger,
                    "Quản lý thông báo"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Gian hàng quan tâm"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý điểm bán"
            ))
        }

        if (UserDataManager.currentType == "Thành viên") {
            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Gian hàng quan tâm"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Sản phẩm quan tâm"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý vé thăm quan"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOTIFICATION,
                    R.drawable.ic_finger,
                    "Quản lý thông báo"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý điểm bán"
            ))
        }

        if (UserDataManager.currentType == "Chủ hội chợ") {
            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý thành viên"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý gian hàng"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý thương hiệu"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý sản phẩm"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý tham quan"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOTIFICATION,
                    R.drawable.ic_finger,
                    "Quản lý thông báo"
            ))

            val childs = ArrayList<AccountMenuItem>()
            childs.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý tin tức"
            ))

            childs.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý thông tin chung"
            ))

            childs.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý hỏi đáp"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý thổng hợp",
                    childs
            ))


            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Quản lý quản trị viên"
            ))

            items.add(AccountMenuItem(
                    Const.AccountAction.ACTION_NOT_AVALIBLE,
                    R.drawable.ic_finger,
                    "Cấu hình giao diện"
            ))
        }

        items.add(AccountMenuItem(
                Const.AccountAction.ACTION_REPORT,
                R.drawable.ic_finger,
                "Báo lỗi - Đóng góp"
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
        addDisposable(authService.logout("android")
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

        addDisposable(authService.changePassword(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        changePassword.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    fun getOTP(phone: String) {
        val fields = mutableMapOf<String, Any>()
        fields.put("phone", phone)

        addDisposable(noAuthService.getOTP(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        getOTP.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

                }))
    }

}
