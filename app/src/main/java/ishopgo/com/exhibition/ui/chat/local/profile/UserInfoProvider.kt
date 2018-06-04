package ishopgo.com.exhibition.ui.chat.local.profile

interface UserInfoProvider {

    fun provideName(): CharSequence
    fun providePhone(): CharSequence
    fun provideCover(): CharSequence
    fun provideAvatar(): CharSequence
    fun provideDob(): CharSequence
    fun provideEmail(): CharSequence
    fun provideCompany(): CharSequence
    fun provideRegion(): CharSequence
    fun provideAddress(): CharSequence
    fun provideType(): CharSequence
    fun provideJoinedDate(): CharSequence

}
