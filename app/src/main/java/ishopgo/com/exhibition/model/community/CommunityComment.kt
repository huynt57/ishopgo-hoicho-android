package ishopgo.com.exhibition.model.community

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData

/**
 * Created by hoangnh on 5/4/2018.
 */
class CommunityComment : IdentityData() {
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("account_name")
    @Expose
    var accountName: String? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Long = 0L
    @SerializedName("account_image")
    @Expose
    var accountImage: String? = null
    @SerializedName("post_id")
    @Expose
    var postId: Long? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("images")
    @Expose
    var images: MutableList<String>? = null
    @SerializedName("comment_count")
    @Expose
    var commentCount: Int? = null
//    @SerializedName("last_comment")
//    @Expose
//    var lastComment: Long = 0
}