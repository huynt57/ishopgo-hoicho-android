package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class IcheckReview{
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("object_id")
    @Expose
    var objectId: Int? = null
    @SerializedName("star")
    @Expose
    var star: Int? = null
    @SerializedName("attachments")
    @Expose
    var attachments: List<Any>? = null
    @SerializedName("icheck_id")
    @Expose
    var icheckId: String? = null
    @SerializedName("comment_count")
    @Expose
    var commentCount: Int? = null
    @SerializedName("deleted_at")
    @Expose
    var deletedAt: Any? = null
    @SerializedName("active")
    @Expose
    var active: Boolean? = null
//    @SerializedName("createdAt")
//    @Expose
//    var createdAt: Long? = null
//    @SerializedName("updatedAt")
//    @Expose
//    var updatedAt: Long? = null
    @SerializedName("owner")
    @Expose
    var owner: IcheckReviewOwner? = null
    @SerializedName("like_count")
    @Expose
    var likeCount: Int? = null
}