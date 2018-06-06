package ishopgo.com.exhibition.ui.main.boothfollow

import android.text.Spanned

interface BoothFollowProvider {
    fun provideName(): Spanned
    fun providePhone(): String
    fun provideAddress(): String
    fun provideNumberProduct(): Spanned
}