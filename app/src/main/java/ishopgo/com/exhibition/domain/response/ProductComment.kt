package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class ProductComment : IdentityData() {
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Long = 0L
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
    @SerializedName("rate")
    @Expose
    var rate: Float? = null

}