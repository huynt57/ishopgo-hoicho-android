package ishopgo.com.exhibition.ui.main.profile

import android.arch.lifecycle.MutableLiveData
import ishopgo.com.exhibition.app.AppComponent
import ishopgo.com.exhibition.domain.request.Request
import ishopgo.com.exhibition.ui.base.BaseApiViewModel

/**
 * Created by xuanhong on 4/24/18. HappyCoding!
 */
class ProfileViewModel : BaseApiViewModel(), AppComponent.Injectable {

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    var userInfo = MutableLiveData<ProfileProvider>()

    fun loadUserProfile() {
        userInfo.postValue(object : ProfileProvider {
            override fun provideAvatar(): String {
                return "https://lh3.googleusercontent.com/-B212qdip-ls/V1a_aouWG1I/AAAAAAAAAfA/pqqn9gV9tcIos_ybMhM_xLmFORG_ZHEowCEwYBhgL/w278-h280-p/10275486_934954516552276_3867031212727261639_o.jpg"
            }

            override fun providePhone(): String {
                return "0974427143"
            }

            override fun provideName(): String {
                return "Vương Xuân Hồng"
            }

            override fun provideDob(): String {
                return "28/05/1991"
            }

            override fun provideEmail(): String {
                return "vuongxuanhong@gmail.com"
            }

            override fun provideCompany(): String {
                return "iShopgo"
            }

            override fun provideRegion(): String {
                return "Hà Nội"
            }

            override fun provideAddress(): String {
                return "68 Dương Đình Nghệ, Nam Từ Liêm, Hà Nội"
            }

            override fun provideAccountType(): String {
                return "Chủ gian hàng"
            }

            override fun provideJoinedDate(): String {
                return "14/04/2018"
            }

        })
    }

    var profileUpdated = MutableLiveData<ProfileProvider>()

    fun updateProfile(params: Request) {
        profileUpdated.postValue(object : ProfileProvider {
            override fun provideAvatar(): String {
                return "https://lh3.googleusercontent.com/-B212qdip-ls/V1a_aouWG1I/AAAAAAAAAfA/pqqn9gV9tcIos_ybMhM_xLmFORG_ZHEowCEwYBhgL/w278-h280-p/10275486_934954516552276_3867031212727261639_o.jpg"
            }

            override fun providePhone(): String {
                return "0974427143"
            }

            override fun provideName(): String {
                return "Vương Xuân Hồng"
            }

            override fun provideDob(): String {
                return "28/05/1991"
            }

            override fun provideEmail(): String {
                return "vuongxuanhong@gmail.com"
            }

            override fun provideCompany(): String {
                return "iShopgo"
            }

            override fun provideRegion(): String {
                return "Hà Nội"
            }

            override fun provideAddress(): String {
                return "68 Dương Đình Nghệ, Nam Từ Liêm, Hà Nội"
            }

            override fun provideAccountType(): String {
                return "Chủ gian hàng"
            }

            override fun provideJoinedDate(): String {
                return "14/04/2018"
            }

        })

    }
}