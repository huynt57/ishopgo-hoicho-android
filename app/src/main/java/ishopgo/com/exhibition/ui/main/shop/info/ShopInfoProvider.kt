package ishopgo.com.exhibition.ui.main.shop.info

/**
 * Created by xuanhong on 4/22/18. HappyCoding!
 */
interface ShopInfoProvider {

    fun provideName(): String
    fun provideImage(): String
    fun providePIC(): String
    fun provideProductCount(): Int
    fun provideJoinedDate(): String
    fun provideRegion(): String
    fun provideRating(): Int
    fun provideClickCount(): Int
    fun provideShareCount(): Int
    fun provideDescription(): String
    fun provideSalePoints(): List<SalePointProvider>

}