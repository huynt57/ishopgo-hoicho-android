package ishopgo.com.exhibition.ui.main.notification.detail

import ishopgo.com.exhibition.domain.response.Notification
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime

/**
 * Created by xuanhong on 6/16/18. HappyCoding!
 */
class ConverterNotificationDetail: Converter<Notification, NotificationDetailProvider> {

    override fun convert(from: Notification): NotificationDetailProvider {
        return object: NotificationDetailProvider {
            override fun provideImage(): CharSequence {
                return if ("Hệ thống".equals(from.sender, true)) "" else from.accountImage ?: ""
            }

            override fun provideContent(): CharSequence {
                return from.title ?: ""
            }

            override fun provideCreatedAt(): CharSequence {
                return from.createdAt?.asDateTime() ?: ""
            }

            override fun provideSender(): CharSequence {
                return from.sender ?: ""
            }

            override fun provideWebContent(): String {
                return from.content ?: ""
            }

        }
    }


}