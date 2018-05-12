package ishopgo.com.exhibition.ui.main.productmanager

interface ProductManagerProvider {
    fun provideId(): Long
    fun provideName(): String
    fun provideImage(): String
    fun provideCode(): String
    fun provideTTPrice(): String
    fun providePrice(): String
    fun provideStatus(): String
    fun provideDepartment(): String
}