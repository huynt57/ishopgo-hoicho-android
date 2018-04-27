package ishopgo.com.exhibition.ui.main.home.search.shop

/**
 * Created by xuanhong on 4/27/18. HappyCoding!
 */
interface SearchShopResultProvider {

    fun provideName(): String
    fun provideProductCount(): String
    fun provideJoinedDate(): String

}