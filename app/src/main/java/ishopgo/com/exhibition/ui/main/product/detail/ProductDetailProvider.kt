package ishopgo.com.exhibition.ui.main.product.detail

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
interface ProductDetailProvider {

    fun provideProductImage(): String
    fun provideProductName(): String
    fun provideProductPrice(): String
    fun provideProductBrand(): String
    fun provideProductShortDescription(): String
    fun provideShopName(): String
    fun provideShopRegion(): String
    fun provideShopProductCount(): Int
    fun provideShopRateCount(): Int
    fun provideShopPhone(): String
    fun provideLiked(): Boolean
    fun provideShopAddress(): String
    fun provideFollowed(): Boolean

    fun provideProductLikeCount(): Int
    fun provideProductCommentCount(): Int
    fun provideProductShareCount(): Int
    fun provideProductLinkAffiliate(): String

}