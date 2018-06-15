package ishopgo.com.exhibition.ui.main.home.post

import ishopgo.com.exhibition.model.post.PostObject
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asHtml

/**
 * Created by xuanhong on 6/13/18. HappyCoding!
 */
class PostConverter : Converter<PostObject, LatestPostProvider> {

    override fun convert(from: PostObject): LatestPostProvider {
        return object : LatestPostProvider {
            override fun provideAvatar(): CharSequence {
                return from.image ?: ""
            }

            override fun provideTitle(): CharSequence {
                return from.name ?: ""
            }

            override fun provideTime(): CharSequence {
                return "${from.createdAt?.asDateTime() ?: ""}".asHtml()
            }

            override fun provideShortDescription(): CharSequence {
                return from.shortContent?.asHtml() ?: ""
            }
        }
    }

}