package ishopgo.com.exhibition.ui.community

/**
 * Created by hoangnh on 4/23/2018.
 */
interface CommunityProvider {
    fun providerUserName(): String
    fun providerId(): Long
    fun providerUserAvatar(): String
    fun provideContent(): String
    fun provideTime(): String
    fun provideLikeCount(): Int
    fun provideLiked(): Boolean
    fun provideCommentCount(): Int
    fun provideShareCount(): Int
    fun provideProduct(): CommunityProductProvider?
    fun provideListImage(): MutableList<String>
}