package ishopgo.com.exhibition.ui.main.ticket

import android.text.Spanned

interface TicketProvider {
    fun provideName(): Spanned
    fun provideEmail(): Spanned
    fun provideTicketName(): String
    fun provideCode(): String
    fun providePhone(): Spanned
    fun provideAddress(): Spanned
    fun provideBanner(): String
    fun provideCreateAt(): Spanned
}