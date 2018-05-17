package ishopgo.com.exhibition.ui.main.membermanager

interface MemberManagerProvider {
    fun provideName(): String
    fun providePhone(): String
    fun provideEmail(): String
    fun provideRegion(): String
    fun provideBooth(): String
}