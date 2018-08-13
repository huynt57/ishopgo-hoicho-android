package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class IcheckReviewOwner : IdentityData() {
    @SerializedName("icheck_id")
    @Expose
    var icheckId: String? = null
    @SerializedName("social_id")
    @Expose
    var socialId: String? = null
    @SerializedName("avatar")
    @Expose
    var avatar: String? = null
    @SerializedName("social_type")
    @Expose
    var socialType: String? = null
    @SerializedName("verification_type")
    @Expose
    var verificationType: Int? = null
    @SerializedName("social_name")
    @Expose
    var socialName: String? = null
    @SerializedName("location_lat")
    @Expose
    var locationLat: Double? = null
    @SerializedName("location_lgn")
    @Expose
    var locationLgn: Double? = null
    @SerializedName("is_online")
    @Expose
    var isOnline: Int? = null
    @SerializedName("cover")
    @Expose
    var cover: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("online")
    @Expose
    var online: Boolean? = null
    @SerializedName("follower_count")
    @Expose
    var followerCount: Int? = null
    @SerializedName("following_count")
    @Expose
    var followingCount: Int? = null
    @SerializedName("point")
    @Expose
    var point: Int? = null
}