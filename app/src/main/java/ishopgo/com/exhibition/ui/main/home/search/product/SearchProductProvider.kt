package ishopgo.com.exhibition.ui.main.home.search.product

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
interface SearchProductProvider {
    fun provideImage(): String
    fun provideName(): String
    fun provideCode(): String

}