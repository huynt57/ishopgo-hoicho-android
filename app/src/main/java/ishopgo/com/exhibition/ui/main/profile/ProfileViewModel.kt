package ishopgo.com.exhibition.ui.main.profile

import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.BaseSingleObserver
import ishopgo.com.exhibition.model.Profile
import ishopgo.com.exhibition.model.UserDataManager
import ishopgo.com.exhibition.ui.base.BaseApiViewModel
import ishopgo.com.exhibition.ui.widget.Toolbox
import okhttp3.MultipartBody

/**
 * Created by xuanhong on 4/24/18. HappyCoding!
 */
class ProfileViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var userInfo = MutableLiveData<ProfileProvider>()

    fun loadUserProfile() {

        addDisposable(apiService.getProfile(UserDataManager.currentUserId)
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Profile>() {
                    override fun success(data: Profile?) {
                        userInfo.postValue(object : ProfileProvider {
                            override fun provideAvatar(): String {
                                return data?.image ?: ""
                            }

                            override fun providePhone(): String {
                                return data?.phone ?: ""
                            }

                            override fun provideName(): String {
                                return data?.name ?: ""
                            }

                            override fun provideDob(): String {
                                return Toolbox.formatApiDate(data?.birthday ?: "")
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
                                return Toolbox.formatApiDateTime(data?.createdAt ?: "")
                            }

                        })
                    }


                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }

    var profileUpdated = MutableLiveData<ProfileProvider>()

    fun updateProfile(name:String, dob:String, email:String, company:String, region:String, address:String, image:MultipartBody.Part?) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("birthday", dob)
                .addFormDataPart("email", email)
                .addFormDataPart("company_store", company)
                .addFormDataPart("region", region)
                .addFormDataPart("address", address)
        if (image != null) {
            builder.addPart(image)
        }
        addDisposable(apiService.updateProfile(builder.build())
                .subscribeOn(Schedulers.single())
                .subscribeWith(object : BaseSingleObserver<Profile>() {
                    override fun success(data: Profile?) {
                        profileUpdated.postValue(object : ProfileProvider {
                            override fun provideAvatar(): String {
                                UserDataManager.currentUserAvatar = data?.image ?: ""
                                return data?.image ?: ""
                            }

                            override fun providePhone(): String {
                                UserDataManager.currentUserPhone = data?.phone ?: ""
                                return data?.phone ?: ""
                            }

                            override fun provideName(): String {
                                UserDataManager.currentUserName = data?.name ?: ""
                                return data?.name ?: ""
                            }

                            override fun provideDob(): String {
                                return Toolbox.formatApiDate(data?.birthday ?: "")
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
                                return Toolbox.formatApiDateTime(data?.createdAt ?: "")
                            }

                        })
                    }

                    override fun failure(status: Int, message: String) {
                        resolveError(status, message)
                    }
                }))
    }
}