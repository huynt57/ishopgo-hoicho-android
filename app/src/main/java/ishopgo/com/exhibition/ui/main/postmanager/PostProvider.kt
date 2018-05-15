package ishopgo.com.exhibition.ui.main.postmanager

interface PostProvider {
    fun provideTitle(): String
    fun provideTime(): String
    fun provideCategoryName(): String
}