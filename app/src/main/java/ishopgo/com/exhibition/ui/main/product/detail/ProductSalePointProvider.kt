package ishopgo.com.exhibition.ui.main.product.detail

interface ProductSalePointProvider {
    fun provideName(): String
    fun provideAddress(): String
    fun providePrice(): String
    fun providePhone(): String
}