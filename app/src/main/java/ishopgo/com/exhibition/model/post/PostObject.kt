package ishopgo.com.exhibition.model.post

import android.text.Spanned
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.extensions.asDateTime
import ishopgo.com.exhibition.ui.extensions.asHtml
import ishopgo.com.exhibition.ui.main.home.post.LatestPostProvider
import ishopgo.com.exhibition.ui.main.postmanager.PostProvider


class PostObject : IdentityData(), PostProvider {

    override fun provideTitle(): String {
        return name ?: ""
    }

    override fun provideTime(): Spanned {
        return "${createdAt?.asDateTime() ?: ""} | Đăng bởi <b>${accountName
                ?: ""}</b> | ${statusText ?: ""}".asHtml()
    }

    override fun provideCategoryName(): String {
        return categoryName ?: ""
    }

    @SerializedName("name")
    @Expose
    val name: String? = null
    @SerializedName("short_content")
    @Expose
    val shortContent: String? = null
    @SerializedName("image")
    @Expose
    val image: String? = null
    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null
    @SerializedName("status")
    @Expose
    val status: Int? = null
    @SerializedName("status_text")
    @Expose
    val statusText: String? = null
    @SerializedName("account_name")
    @Expose
    val accountName: String? = null
    @SerializedName("category_id")
    @Expose
    val categoryId: Int? = null
    @SerializedName("category_name")
    @Expose
    val categoryName: String? = null
}

fun PostObject.asLatestPostProvider(): LatestPostProvider {
    return object : LatestPostProvider {
        override fun provideAvatar(): CharSequence {
            return image ?: ""
        }

        override fun provideTitle(): CharSequence {
            return name ?: ""
        }

        override fun provideTime(): CharSequence {
            return "${createdAt?.asDateTime() ?: ""}".asHtml()
        }

        override fun provideShortDescription(): CharSequence {
            return shortContent?.asHtml() ?: ""
        }

    }
}
