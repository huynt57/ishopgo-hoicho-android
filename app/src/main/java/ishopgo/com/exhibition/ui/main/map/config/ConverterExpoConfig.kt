package ishopgo.com.exhibition.ui.main.map.config

import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDate

/**
 * Created by xuanhong on 6/18/18. HappyCoding!
 */
class ConverterExpoConfig : Converter<ExpoConfig, ExpoMapConfigProvider> {

    override fun convert(from: ExpoConfig): ExpoMapConfigProvider {
        return object : ExpoMapConfigProvider {
            override fun avatar(): CharSequence {
                return from.image ?: ""
            }

            override fun name(): CharSequence {
                return from.name ?: ""
            }

            override fun time(): CharSequence {
                return "Thời gian: ${from.startTime?.asDate() ?: ""} - ${from.endTime?.asDate()
                        ?: ""}"
            }

            override fun address(): CharSequence {
                return "Địa điểm: ${from.address ?: ""}"
            }

        }
    }


}