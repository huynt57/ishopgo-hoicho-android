package ishopgo.com.exhibition.ui.main.profile

/**
 * Created by xuanhong on 4/24/18. HappyCoding!
 */
interface ProfileProvider {

    fun provideAvatar(): String
    fun providePhone(): CharSequence
    fun provideName(): CharSequence
    fun provideDob(): CharSequence
    fun provideEmail(): CharSequence
    fun provideCompany(): CharSequence
    fun provideRegion(): CharSequence
    fun provideAddress(): CharSequence
    fun provideAccountType(): CharSequence
    fun provideJoinedDate(): CharSequence
    fun provideIntroduction(): CharSequence

}