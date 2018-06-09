package ishopgo.com.exhibition.ui.main.profile

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.Profile
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.extensions.Toolbox
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asPhone
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
                        userInfo.postValue(object : ProfileProvider {
                            override fun provideIntroduction(): String {
                                return data?.introduction ?: ""
                            }

                            override fun provideAvatar(): String {
                                return data?.image ?: ""
                            }

                            override fun providePhone(): String {
                                return data?.phone?.asPhone() ?: ""
                            }

                            override fun provideName(): String {
                                return data?.name ?: ""
                            }

                            override fun provideDob(): String {
                                return data?.birthday?.asDate() ?: ""
                            }

                            override fun provideEmail(): String {
                                return data?.email ?: ""
                            }

                            override fun provideCompany(): String {
                                return data?.company ?: ""
                            }

                            override fun provideRegion(): String {
                                return data?.region ?: ""
                            }

                            override fun provideAddress(): String {
                                return data?.address ?: ""
                            }

                            override fun provideAccountType(): String {
                                return data?.typeText ?: ""
                            }

                            override fun provideJoinedDate(): String {
                                return data?.createdAt?.asDateTime() ?: ""
                            }

                        })
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
                .addFormDataPart("name", name)
                .addFormDataPart("birthday", dob)
                .addFormDataPart("email", email)
                .addFormDataPart("company_store", company)
                .addFormDataPart("region", region)
                .addFormDataPart("address", address)
                .addFormDataPart("introduction", introduction)

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
                        profileUpdated.postValue(object : ProfileProvider {
                            override fun provideIntroduction(): String {
                                return data?.introduction ?: ""
                            }

                            override fun provideAvatar(): String {
                                UserDataManager.currentUserAvatar = data?.image ?: ""
                                return data?.image ?: ""
                            }

                            override fun providePhone(): String {
                                UserDataManager.currentUserPhone = data?.phone?.asPhone() ?: ""
                                return data?.phone?.asPhone() ?: ""
                            }

                            override fun provideName(): String {
                                UserDataManager.currentUserName = data?.name ?: ""
                                return data?.name ?: ""
                            }

                            override fun provideDob(): String {
                                return data?.birthday?.asDate() ?: ""
                            }

                            override fun provideEmail(): String {
                                return data?.email ?: ""
                            }

                            override fun provideCompany(): String {
                                return data?.company ?: ""
                            }

                            override fun provideRegion(): String {
                                return data?.region ?: ""
                            }

                            override fun provideAddress(): String {
                                return data?.address ?: ""
                            }

                            override fun provideAccountType(): String {
                                return data?.typeText ?: ""
                            }

                            override fun provideJoinedDate(): String {
                                return data?.createdAt?.asDateTime() ?: ""
                            }

                        })
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }
}