package ishopgo.com.exhibition.ui.community

/**
 * Created by hoangnh on 4/23/2018.
 */
interface CommunityProvider {
    fun userName(): String
    fun userAvatar(): String
    fun currentUserAvatar(): String
    fun provideContent(): String
    fun provideTime(): String
    fun provideLikeCount(): Int
    fun provideCommentCount(): Int
    fun provideShareCount(): Int
    fun provideProductName(): String
    fun provideProductCode(): String
    fun provideProductPrice(): String
    fun provideProductListImage(): MutableList<CommunityImageProvider>?
    fun provideType(): String
}