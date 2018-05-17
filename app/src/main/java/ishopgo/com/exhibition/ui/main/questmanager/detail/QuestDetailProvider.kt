package ishopgo.com.exhibition.ui.main.questmanager.detail

interface QuestDetailProvider {
    fun provideTitle(): String
    fun provideTime(): String
    fun provideAnswer(): String
}