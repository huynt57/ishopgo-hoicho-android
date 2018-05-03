package ishopgo.com.exhibition.ui.community

/**
 * Created by hoangnh on 4/24/2018.
 */
interface CommunityProductProvider {
    fun providerId(): Long
    fun providerName(): String
    fun providerPrice(): String
    fun providerImage(): String
}