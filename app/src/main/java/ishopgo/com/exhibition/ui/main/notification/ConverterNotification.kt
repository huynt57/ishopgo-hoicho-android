package ishopgo.com.exhibition.ui.main.notification

import ishopgo.com.exhibition.domain.response.Notification
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime

/**
 * Created by xuanhong on 6/16/18. HappyCoding!
 */
class ConverterNotification : Converter<Notification, NotificationProvider> {

    override fun convert(from: Notification): NotificationProvider {
        return object : NotificationProvider {
            override fun provideImage(): CharSequence {
                return if ("Hệ thống".equals(from.sender, true)) "" else from.accountImage ?: ""
            }

            override fun provideContent(): CharSequence {
                return from.shortDescription ?: ""
            }

            override fun provideCreatedAt(): CharSequence {
                return from.createdAt?.asDateTime() ?: ""
            }

            override fun provideSender(): CharSequence {
                return from.sender ?: ""
            }

            override fun provideWasRed(): Boolean {
                return from.isRead == 1
            }
        }
    }
}