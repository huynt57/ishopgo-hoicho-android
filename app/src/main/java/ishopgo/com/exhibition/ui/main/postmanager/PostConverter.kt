package ishopgo.com.exhibition.ui.main.postmanager

import ishopgo.com.exhibition.model.post.PostObject
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.extensions.asHtml

/**
 * Created by xuanhong on 6/14/18. HappyCoding!
 */
class PostConverter : Converter<PostObject, PostProvider> {

    override fun convert(from: PostObject): PostProvider {
        return object : PostProvider {
            override fun provideOwner(): CharSequence {
                return from.accountName ?: ""
            }

            override fun provideCategory(): CharSequence {
                return from.categoryName ?: ""
            }

            override fun provideAvatar(): CharSequence {
                return from.image ?: ""
            }

            override fun provideTitle(): CharSequence {
                return from.name ?: ""
            }

            override fun provideShortDescription(): CharSequence {
                return from.shortContent?.asHtml() ?: ""
            }

            override fun provideTime(): CharSequence {
                return from.createdAt?.asDate() ?: ""
            }

        }
    }


}