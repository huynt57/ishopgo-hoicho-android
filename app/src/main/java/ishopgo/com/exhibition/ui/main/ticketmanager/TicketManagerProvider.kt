package ishopgo.com.exhibition.ui.main.ticketmanager

interface TicketManagerProvider {
    fun provideBoothName(): String
    fun provideTicketAddress(): String
    fun provideTicketCode(): String
    fun provideBoothPhone(): String
    fun provideDateScan(): String
}