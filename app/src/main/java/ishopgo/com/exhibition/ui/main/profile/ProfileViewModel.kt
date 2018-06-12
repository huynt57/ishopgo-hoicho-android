package ishopgo.com.exhibition.ui.main.profile

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.Profile
import ishopgo.com.exhibition.model.Region
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.extensions.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by xuanhong on 4/24/18. HappyCoding!
 */
class ProfileViewModel : BaseApiViewModel(), AppComponent.Injectable {

    @Inject
    lateinit var appContext: Application

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var userInfo = MutableLiveData<ProfileProvider>()

    fun loadUserProfile() {
        addDisposable(authService.getProfile(UserDataManager.currentUserId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Profile>() {
                    override fun success(data: Profile?) {
                        userInfo.postValue(data)
                    }


                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var profileUpdated = MutableLiveData<ProfileProvider>()

    fun updateProfile(name: String, dob: String, email: String, company: String, region: String, address: String, introduction: String, image: String) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        if (name.isNotEmpty()) builder.addFormDataPart("name", name)
        if (dob.isNotEmpty()) builder.addFormDataPart("birthday", dob)
        if (email.isNotEmpty()) builder.addFormDataPart("email", email)
        if (company.isNotEmpty()) builder.addFormDataPart("company_store", company)
        if (region.isNotEmpty()) builder.addFormDataPart("region", region)
        if (address.isNotEmpty()) builder.addFormDataPart("address", address)
        if (introduction.isNotEmpty()) builder.addFormDataPart("introduction", introduction)

        if (image.isNotEmpty()) {
            val imageFile = File(appContext.cacheDir, "avatar_" + System.currentTimeMillis() + ".jpg")
            imageFile.deleteOnExit()
            Toolbox.reEncodeBitmap(appContext, Uri.parse(image), 640, Uri.fromFile(imageFile))
            val imageBody = RequestBody.create(MultipartBody.FORM, imageFile)
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageBody)
            builder.addPart(imagePart)
        }

        addDisposable(authService.updateProfile(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Profile>() {
                    override fun success(data: Profile?) {
                        UserDataManager.currentUserName = data?.name ?: ""
                        UserDataManager.currentUserAvatar = data?.image ?: ""
                        UserDataManager.currentType = data?.typeTextExpo ?: ""

                        profileUpdated.postValue(data)
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var loadRegion = MutableLiveData<MutableList<Region>>()

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
}