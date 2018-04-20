package ishopgo.com.exhibition.ui.main.product

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
interface ProductProvider {
    fun provideImage(): String
    fun provideName(): String
    fun providePrice(): String
    fun provideMarketPrice(): String

}