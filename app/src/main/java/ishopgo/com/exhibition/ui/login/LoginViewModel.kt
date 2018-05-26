package ishopgo.com.exhibition.ui.login

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.model.survey.CheckSurvey
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import okhttp3.MultipartBody

/**
 * Created by hoangnh on 4/23/2018.
 */
class LoginViewModel : BaseApiViewModel(), AppComponent.Injectable {
    var loginSuccess = MutableLiveData<User>()
    var registerSuccess = MutableLiveData<Boolean>()
    var getOTP = MutableLiveData<Boolean>()
    var changNewPassword = MutableLiveData<Boolean>()
    var loadRegion = MutableLiveData<MutableList<Region>>()
    var getUserByPhone = MutableLiveData<PhoneInfo>()
    var checkSurveySusscess = MutableLiveData<Int>()


    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun loginAccount(phone: String, password: String) {
        addDisposable(noAuthService.login(phone, password, Const.ID_APP, "")
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<User>() {
                    override fun success(data: User?) {

                        UserDataManager.accessToken = data?.token ?: ""
                        UserDataManager.currentUserId = data?.id ?: 0
                        UserDataManager.currentUserPhone = phone
                        UserDataManager.currentUserName = data?.name ?: ""
                        UserDataManager.currentUserAvatar = data?.image ?: ""
                        UserDataManager.currentType = data?.type ?: ""

                        loginSuccess.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    fun registerAccount(phone: String, email: String, fullname: String, company: String, birthday: String,
                        region: String, address: String, password: String, register_type: String) {

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)
                .addFormDataPart("email", email)
                .addFormDataPart("name", fullname)
                .addFormDataPart("company_store", company)
                .addFormDataPart("birthday", birthday)
                .addFormDataPart("region", region)
                .addFormDataPart("address", address)
                .addFormDataPart("password", password)
                .addFormDataPart("type", register_type)

        addDisposable(noAuthService.register(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        registerSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    fun getOTP(phone: String) {
        val fields = mutableMapOf<String, Any>()
        fields["phone"] = phone

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

    fun changeNewPassword(phone: String, otp: String, password: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)
                .addFormDataPart("otp", otp)
                .addFormDataPart("password", password)

        addDisposable(authService.changePassword(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        changNewPassword.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    fun loadRegion() {
        addDisposable(isgService.getRegions()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<MutableList<Region>>() {
                    override fun success(data: MutableList<Region>?) {
                        loadRegion.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    fun loadUserByPhone(phone: String) {
        val fields = mutableMapOf<String, Any>()
        fields["phone"] = phone
        addDisposable(isgService.getUserByPhone(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<PhoneInfo>() {
                    override fun success(data: PhoneInfo?) {
                        getUserByPhone.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                    }
                }))
    }

    fun checkSurvey() {
        addDisposable(authService.checkSurvey()
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<CheckSurvey>() {
                    override fun success(data: CheckSurvey?) {
                        UserDataManager.currentSurvey = data?.status ?: 0
                        checkSurveySusscess.postValue(data?.status)
                    }

                    override fun failure(status: Int, message: String) {
                    }
                }))
    }
}