package ishopgo.com.exhibition.ui.main.boothfollow


interface BoothFollowProvider {
    fun provideName(): String
    fun providePhone(): String
    fun provideAddress(): String
    fun provideNumberProduct(): String
    fun provideMemberCNT(): String
    fun provideQrCode(): String
}