package ishopgo.com.exhibition.ui.main.postmanager

interface PostProvider {
    fun provideAvatar(): CharSequence
    fun provideTitle(): CharSequence
    fun provideTime(): CharSequence
    fun provideOwner(): CharSequence
    fun provideShortDescription(): CharSequence
    fun provideCategory(): CharSequence
}