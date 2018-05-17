package ishopgo.com.exhibition.ui.main.brandmanager

interface BrandManagerProvider {
    fun provideName(): String
    fun provideLogo(): String
    fun provideIsFeatured(): Boolean
}