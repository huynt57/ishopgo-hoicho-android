package ishopgo.com.exhibition.ui.main.notification

/**
 * Created by hoangnh on 5/7/2018.
 */
interface NotificationProvider {
    fun provideId(): Long
    fun provideAccount(): String
    fun provideTitle(): String
    fun provideImage(): String
    fun provideContent(): String
    fun provideShortDescription(): String
    fun provideCreatedAt(): String
    fun provideSender(): String
    fun provideIsRead(): Int
}