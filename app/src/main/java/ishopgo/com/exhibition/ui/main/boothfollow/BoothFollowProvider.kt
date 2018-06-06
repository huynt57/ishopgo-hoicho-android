package ishopgo.com.exhibition.ui.main.boothfollow

import android.text.Spanned

interface BoothFollowProvider {
    fun provideName(): Spanned
    fun providePhone(): Spanned
    fun provideAddress(): Spanned
    fun provideNumberProduct(): Spanned
}