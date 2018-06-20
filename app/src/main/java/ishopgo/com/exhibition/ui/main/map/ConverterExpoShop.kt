package ishopgo.com.exhibition.ui.main.map

import ishopgo.com.exhibition.domain.response.ExpoShop
import ishopgo.com.exhibition.ui.base.widget.Converter

/**
 * Created by xuanhong on 6/18/18. HappyCoding!
 */
class ConverterExpoShop : Converter<ExpoShop, ExpoShopProvider> {

    override fun convert(from: ExpoShop): ExpoShopProvider {
        return object : ExpoShopProvider {
            override fun isSetup(): Boolean {
                return from.booth != null
            }

            override fun provideName(): CharSequence {
                return from.booth?.name ?: ""
            }

            override fun provideNumber(): CharSequence {
                return "${from.priority ?: 0}"
            }

            override fun provideRegion(): CharSequence {
                return from.booth?.city ?: ""
            }

        }
    }


}