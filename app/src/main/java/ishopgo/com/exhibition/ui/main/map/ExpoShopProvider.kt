package ishopgo.com.exhibition.ui.main.map

/**
 * Created by xuanhong on 6/11/18. HappyCoding!
 */
interface ExpoShopProvider {

    fun provideName(): CharSequence
    fun provideNumber(): CharSequence
    fun provideRegion(): CharSequence
    fun isSetup(): Boolean

}