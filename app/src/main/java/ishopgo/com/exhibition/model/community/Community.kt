package ishopgo.com.exhibition.model.community

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


/**
 * Created by hoangnh on 5/3/2018.
 */
class Community : IdentityData() {
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("shop_id")
    @Expose
    var shopId: Long? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Long? = null
    @SerializedName("account_name")
    @Expose
    var accountName: String? = null
    @SerializedName("account_image")
    @Expose
    var accountImage: String? = null
    @SerializedName("product")
    @Expose
    var product: CommunityProduct? = null
    @SerializedName("images")
    @Expose
    var images: MutableList<String>? = null
    @SerializedName("liked")
    @Expose
    var liked: Int? = null
    @SerializedName("like_count")
    @Expose
    var likeCount: Int? = null
    @SerializedName("comment_count")
    @Expose
    var commentCount: Int? = null
    @SerializedName("share_count")
    @Expose
    var shareCount: Int? = null
}