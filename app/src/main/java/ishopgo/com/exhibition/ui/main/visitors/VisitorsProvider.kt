package ishopgo.com.exhibition.ui.main.visitors

interface VisitorsProvider {
    fun provideName(): CharSequence
    fun provideAvatar(): CharSequence
    fun providePhone(): CharSequence
    fun provideEmail(): CharSequence
}