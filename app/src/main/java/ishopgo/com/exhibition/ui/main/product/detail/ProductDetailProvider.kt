package ishopgo.com.exhibition.ui.main.product.detail

/**
 * Created by xuanhong on 4/20/18. HappyCoding!
 */
interface ProductDetailProvider {

    fun provideProductImage(): CharSequence
    fun provideProductName(): CharSequence
    fun provideProductPrice(): CharSequence
    fun provideProductBrand(): CharSequence
    fun provideProductShortDescription(): CharSequence
    fun provideShopName(): CharSequence
    fun provideShopRegion(): CharSequence
    fun provideShopProductCount(): Int
    fun provideShopRateCount(): Int
    fun provideShopPhone(): CharSequence
    fun provideLiked(): Boolean
    fun provideShopAddress(): CharSequence
    fun provideFollowed(): Boolean

    fun provideProductLikeCount(): Int
    fun provideProductCommentCount(): Int
    fun provideProductShareCount(): Int
    fun provideProductLinkAffiliate(): CharSequence

    fun provideViewWholesale(): Boolean
    fun provideWholesale(): CharSequence
    fun provideWholesaleLimit(): CharSequence

}