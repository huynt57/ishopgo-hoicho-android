package ishopgo.com.exhibition.ui.chat.local.profile

interface UserInfoProvider {

    fun provideAvatar(): CharSequence
    fun provideCover(): CharSequence
    fun provideName(): CharSequence
    fun provideBoothName(): String

    fun info(): CharSequence
}
