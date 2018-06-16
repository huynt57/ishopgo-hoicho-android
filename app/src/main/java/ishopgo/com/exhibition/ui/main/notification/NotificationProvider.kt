package ishopgo.com.exhibition.ui.main.notification

/**
 * Created by hoangnh on 5/7/2018.
 */
interface NotificationProvider {
    fun provideImage(): CharSequence
    fun provideContent(): CharSequence
    fun provideCreatedAt(): CharSequence
    fun provideSender(): CharSequence
    fun provideWasRed(): Boolean
}