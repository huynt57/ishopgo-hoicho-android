package ishopgo.com.exhibition.ui.main.product.detail

import android.text.Spanned

interface ProductSalePointProvider {
    fun provideName(): Spanned
    fun provideAddress(): String
    fun providePrice(): String
    fun providePhone(): String
}