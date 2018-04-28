package ishopgo.com.exhibition.ui.login

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.BaseDataResponse
import ishopgo.com.exhibition.model.LoginResponse
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import okhttp3.MultipartBody

/**
 * Created by hoangnh on 4/23/2018.
 */
class LoginViewModel : BaseApiViewModel(), AppComponent.Injectable {
    var loginSuccess = MutableLiveData<LoginResponse>()
    var registerSuccess = MutableLiveData<Boolean>()
    var getOTP = MutableLiveData<Boolean>()
    var changNewPassword = MutableLiveData<Boolean>()
    var loadRegion = MutableLiveData<MutableList<Region>>()

    companion object {
        const val id_app = "hoichone"
    }


    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun loginAccount(phone: String, password: String) {
        addDisposable(apiService.login(phone, password, id_app, "")
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<LoginResponse>() {
                    override fun success(data: LoginResponse?) {
                        if (data == null) {
//                            deleteLoginInfo(info.phone, info.domain)
                            loginSuccess.postValue(data)
                        } else {

//                            saveLoginInfo(info)

                            UserDataManager.accessToken = data.token
                            UserDataManager.currentUserId = data.id
                            UserDataManager.currentUserName = data.name
                            UserDataManager.currentUserAvatar = data.image
                            UserDataManager.currentType = data.type

                            loginSuccess.postValue(data)
                        }
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
                .addFormDataPart("id_app", id_app)
                .addFormDataPart("company_store", company)
                .addFormDataPart("birthday", birthday)
                .addFormDataPart("region", region)
                .addFormDataPart("address", address)
                .addFormDataPart("password", password)
                .addFormDataPart("type", register_type)

        addDisposable(apiService.register(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<BaseDataResponse>() {
                    override fun success(data: BaseDataResponse?) {
                        registerSuccess.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    fun getOTP(phone: String) {
        val fields = mutableMapOf<String, Any>()
        fields.put("id_app", id_app)
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

    fun changeNewPassword(phone: String, otp: String, password: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)
                .addFormDataPart("otp", otp)
                .addFormDataPart("password", password)
                .addFormDataPart("id_app", id_app)

        addDisposable(apiService.changePassword(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<BaseDataResponse>() {
                    override fun success(data: BaseDataResponse?) {
                        changNewPassword.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    fun loadRegion() {
        val listRegion = mutableListOf<Region>()

        for (i in 0..20) {
            val region = Region()
            region.name = "Khu vực $i"
            region.provinceid = i.toString()
            region.type = i.toString()
            listRegion.add(region)
        }

        loadRegion.postValue(listRegion)
    }
}