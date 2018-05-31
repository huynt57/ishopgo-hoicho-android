package ishopgo.com.exhibition.ui.main.home.search.sale_point

import android.text.Spanned

interface SearchSalePointProvider {
    fun provideAddress(): Spanned
    fun provideName(): Spanned
    fun providePhone(): String
    fun provideCountProduct(): String
}