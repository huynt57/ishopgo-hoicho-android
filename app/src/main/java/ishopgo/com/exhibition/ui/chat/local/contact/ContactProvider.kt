package ishopgo.com.exhibition.ui.chat.local.contact

/**
 * Created by xuanhong on 5/23/18. HappyCoding!
 */
interface ContactProvider {

    fun provideAvatar(): String
    fun provideName(): String
    fun providePhone(): String
    fun provideJob(): String

}