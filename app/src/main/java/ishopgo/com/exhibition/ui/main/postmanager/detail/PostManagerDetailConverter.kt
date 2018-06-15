package ishopgo.com.exhibition.ui.main.postmanager.detail

import ishopgo.com.exhibition.model.post.PostObject
import ishopgo.com.exhibition.ui.base.widget.Converter
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.extensions.asHtml

/**
 * Created by xuanhong on 6/14/18. HappyCoding!
 */
class PostManagerDetailConverter: Converter<PostObject, PostManagerDetailProvider> {
    override fun convert(from: PostObject): PostManagerDetailProvider {
        return object: PostManagerDetailProvider {
            override fun provideCategory(): CharSequence {
                return from.categoryName ?: ""
            }

            override fun provideTitle(): CharSequence {
                return from.name ?: ""
            }

            override fun provideInfo(): CharSequence {
                return "${from.createdAt?.asDate() ?: ""} | Đăng bởi <b>${from.accountName}</b>".asHtml()
            }

        }
    }

}