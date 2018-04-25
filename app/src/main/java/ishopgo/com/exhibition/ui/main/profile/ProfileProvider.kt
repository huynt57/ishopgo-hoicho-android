package ishopgo.com.exhibition.ui.main.profile

/**
 * Created by xuanhong on 4/24/18. HappyCoding!
 */
interface ProfileProvider {

    fun provideAvatar(): String
    fun providePhone(): String
    fun provideName(): String
    fun provideDob(): String
    fun provideEmail(): String
    fun provideCompany(): String
    fun provideRegion(): String
    fun provideAddress(): String
    fun provideAccountType(): String
    fun provideJoinedDate(): String

}