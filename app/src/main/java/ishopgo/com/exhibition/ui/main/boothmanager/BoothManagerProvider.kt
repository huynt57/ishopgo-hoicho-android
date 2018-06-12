package ishopgo.com.exhibition.ui.main.boothmanager

interface BoothManagerProvider {
    fun provideName(): String
    fun providePhone(): String
    fun provideRegion(): String
    fun provideNumberProduct(): String
    fun provideMemberCNT(): String
    fun provideQrCode(): String
}