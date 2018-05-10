package ishopgo.com.exhibition.ui.main.salepoint

interface SalePointProvider {
    fun provideId(): Long
    fun provideName(): String
    fun providePhone(): String
    fun provideCity(): String
    fun provideAddress(): String
    fun provideDistrict(): String
    fun providePrice(): String
    fun provideStatus(): String
    fun provideWasStatus(): Boolean
    fun provideProductName(): String
    fun provideManagerPhone(): String
}