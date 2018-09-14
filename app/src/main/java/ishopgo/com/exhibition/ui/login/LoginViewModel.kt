package ishopgo.com.exhibition.ui.login

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.domain.response.LoginFacebook
import ishopgo.com.exhibition.model.*
import ishopgo.com.exhibition.model.survey.CheckSurvey
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by hoangnh on 4/23/2018.
 */
class LoginViewModel : BaseApiViewModel(), AppComponent.Injectable {
    val TAG = "LoginViewModel"

    @SuppressLint("StaticFieldLeak")
    @Inject
    lateinit var appContext: Application

    var loginSuccess = MutableLiveData<User>()
    var loginFacebookSuccess = MutableLiveData<Boolean>()
    var loginFacebookUpdate = MutableLiveData<LoginFacebook>()
    var registerSuccess = MutableLiveData<Boolean>()
    var getOTP = MutableLiveData<Boolean>()
    var changNewPassword = MutableLiveData<Boolean>()
    var updateInfoFb = MutableLiveData<Boolean>()
    var loadRegion = MutableLiveData<MutableList<Region>>()
    var getUserByPhone = MutableLiveData<PhoneInfo>()
    var checkSurveySusscess = MutableLiveData<Int>()
    var loadDistrict = MutableLiveData<MutableList<District>>()

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun loginAccount(phone: String, password: String) {
        addDisposable(noAuthService.login(phone, password, UserDataManager.appId, FirebaseInstanceId.getInstance().token
                ?: "")
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<User>() {
                    override fun success(data: User?) {
                        if (data?.type ?: "" == "Quản trị viên" || data?.type ?: "" == "Nhân viên gian hàng") {
                            loadPermission()
                        }

                        UserDataManager.accessToken = data?.token ?: ""
                        UserDataManager.currentUserId = data?.id ?: 0
                        UserDataManager.currentUserPhone = phone
                        UserDataManager.currentUserName = data?.name ?: ""
                        UserDataManager.currentUserAvatar = data?.image ?: ""
                        UserDataManager.currentBoothId = data?.idBooth ?: -1L
                        UserDataManager.currentType = data?.type ?: ""

                        loginSuccess.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    private fun loadPermission() {
        val fields = mutableMapOf<String, Any>()

        addDisposable(authService.getAccountPermissions(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<MutableList<String>>() {
                    override fun success(data: MutableList<String>?) {
                        Const.listPermission = data ?: mutableListOf()
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                })
        )
    }

    fun registerAccount(phone: String, email: String, fullname: String, company: String,
                        region: String, district: String, address: String, referenceTel: String, password: String, image: String) {

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)
                .addFormDataPart("email", email)
                .addFormDataPart("name", fullname)
                .addFormDataPart("company_store", company)
                .addFormDataPart("region", region)
                .addFormDataPart("district", district)
                .addFormDataPart("address", address)
                .addFormDataPart("password", password)

        if (referenceTel.isNotBlank()) builder.addFormDataPart("refer_phone", referenceTel)

        var imagePart: MultipartBody.Part? = null

        if (image.trim().isNotEmpty()) {
            val imageFile = File(appContext.cacheDir, "avatar_" + System.currentTimeMillis() + ".jpg")
            imageFile.deleteOnExit()
            Toolbox.reEncodeBitmap(appContext, Uri.parse(image), 640, Uri.fromFile(imageFile))
            val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
            imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageBody)
        }

        if (imagePart != null) {
            builder.addPart(imagePart)
        }
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

    fun loadDistrict(province_id: String) {
        val fields = mutableMapOf<String, Any>()
        fields["province_id"] = province_id

        addDisposable(isgService.getDistricts(fields)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<MutableList<District>>() {
                    override fun success(data: MutableList<District>?) {
                        loadDistrict.postValue(data)
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

    fun updateInfoFacebook(phone: String, thanhPho: String, quanHuyen: String, diaChi: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                        .addFormDataPart("phone", phone)
                        .addFormDataPart("name", UserDataManager.currentUserName)

        if (thanhPho.isNotEmpty()) builder.addFormDataPart("region", thanhPho)
        if (quanHuyen.isNotEmpty()) builder.addFormDataPart("district", quanHuyen)
        if (diaChi.isNotEmpty()) builder.addFormDataPart("address", diaChi)


        addDisposable(authService.updateProfile(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Profile>() {
                    override fun success(data: Profile?) {
                        UserDataManager.currentUserId = data?.id ?: 0
                        UserDataManager.currentUserPhone = data?.phone ?: ""
                        UserDataManager.currentUserName = data?.name ?: ""
                        UserDataManager.currentUserAvatar = data?.image ?: ""
                        UserDataManager.currentType = data?.typeTextExpo ?: ""
                        UserDataManager.passLoginFacebook = true

                        updateInfoFb.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    fun loginFacebook(facebookId: String, email: String, image: String, facebookUrl: String, facebookName: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("facebook_id", facebookId)
        if (email.isNotEmpty())
            builder.addFormDataPart("email", email)
        if (image.isNotEmpty())
            builder.addFormDataPart("image", image)
        if (facebookUrl.isNotEmpty())
            builder.addFormDataPart("facebook_url", facebookUrl)
        if (facebookName.isNotEmpty())
            builder.addFormDataPart("facebook_name", facebookName)

        addDisposable(noAuthService.loginFacebook(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<LoginFacebook>() {
                    override fun success(data: LoginFacebook?) {
                        if (data != null)
                            if (data.checkPhone) {
                                if (data.type ?: "" == "Quản trị viên" || data.type ?: "" == "Nhân viên gian hàng") {
                                    loadPermission()
                                }

                                UserDataManager.currentUserId = data.id ?: 0
                                UserDataManager.currentUserPhone = data.phone ?: ""
                                UserDataManager.currentUserName = data.name ?: ""
                                UserDataManager.currentUserAvatar = data.image ?: ""
                                UserDataManager.currentBoothId = data.idBooth ?: -1L
                                UserDataManager.currentType = data.type ?: ""
                                UserDataManager.passLoginFacebook = true
                                UserDataManager.accessToken = data.token ?: ""

                                loginFacebookSuccess.postValue(true)
                            } else {
                                UserDataManager.accessToken = data.token ?: ""
                                UserDataManager.passLoginFacebook = false
                                loginFacebookUpdate.postValue(data)
                            }
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var loggedOut = MutableLiveData<Boolean>()

    fun logout() {
        addDisposable(authService.logout("android")
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Any>() {
                    override fun success(data: Any?) {
                        loggedOut.postValue(true)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }

//                    override fun onError(e: Throwable?) {
//                    }
//
//                    override fun onSuccess(t: Any?) {
//                    }
                })
        )
    }
}