package ishopgo.com.exhibition.ui.main.questmanager

interface QuestProvider {
    fun provideTitle(): String
    fun provideTime(): String
}