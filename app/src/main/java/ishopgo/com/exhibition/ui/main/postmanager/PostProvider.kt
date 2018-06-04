package ishopgo.com.exhibition.ui.main.postmanager

import android.text.Spanned

interface PostProvider {
    fun provideTitle(): String
    fun provideTime(): Spanned
    fun provideCategoryName(): String
}