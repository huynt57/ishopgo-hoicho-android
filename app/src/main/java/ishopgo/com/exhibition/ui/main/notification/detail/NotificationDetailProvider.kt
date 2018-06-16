package ishopgo.com.exhibition.ui.main.notification.detail

/**
 * Created by xuanhong on 6/16/18. HappyCoding!
 */
interface NotificationDetailProvider {

    fun provideImage(): CharSequence
    fun provideContent(): CharSequence
    fun provideCreatedAt(): CharSequence
    fun provideSender(): CharSequence
    fun provideWebContent(): String

}