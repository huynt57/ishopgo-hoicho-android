package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentProvider


/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class ProductComment : IdentityData(), ProductCommentProvider {
    override fun provideName(): String {
        return accountName ?: "unknown"
    }

    override fun provideAvatar(): String {
        return accountImage ?: ""
    }

    override fun provideTime(): String {
        return updatedAt?.asDate() ?: ""
    }

    override fun provideContent(): String {
        return content ?: ""
    }

    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("account_name")
    @Expose
    var accountName: String? = null
    @SerializedName("account_image")
    @Expose
    var accountImage: String? = null
    @SerializedName("product_id")
    @Expose
    var productId: Long = 0
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("images")
    @Expose
    var images: List<Any>? = null
    @SerializedName("comment_count")
    @Expose
    var commentCount: Int? = null
    @SerializedName("last_comment")
    @Expose
    var lastComment: ProductComment? = null

}