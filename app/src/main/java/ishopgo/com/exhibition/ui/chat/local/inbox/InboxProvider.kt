package ishopgo.com.exhibition.ui.chat.local.inbox

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
interface InboxProvider {

    fun provideName(): String
    fun provideAvatar(): String
    fun provideMessage(): String
    fun provideTime(): String

}