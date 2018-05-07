package ishopgo.com.exhibition.ui.community

/**
 * Created by hoangnh on 4/23/2018.
 */
interface CommunityCommentProvider {
    fun providerContent(): String
    fun providerAccountId(): Long
    fun providerAccountName(): String
    fun providerAccountImage(): String
    fun providerPostId(): Long
    fun providerUpdatedAt(): String
    fun providerCreatedAt(): String
    fun providerImages(): MutableList<String>
    fun provideCommentCount(): Long
//    fun provideProduct(): CommunityProductProvider?
}