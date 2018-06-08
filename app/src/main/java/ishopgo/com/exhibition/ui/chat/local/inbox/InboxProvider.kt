package ishopgo.com.exhibition.ui.chat.local.inbox

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
interface InboxProvider {

    fun provideName(): CharSequence
    fun provideAvatar(): CharSequence
    fun provideMessage(): CharSequence
    fun provideTime(): CharSequence
    fun provideWasRead(): Boolean

}