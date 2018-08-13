package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IcheckSalePointDetail {
    @SerializedName("business")
    @Expose
    var business: Int? = null
    @SerializedName("owner")
    @Expose
    var owner: Int? = null
    @SerializedName("city")
    @Expose
    var city: IcheckCountry? = null
    @SerializedName("district")
    @Expose
    var district: IcheckCountry? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("lat")
    @Expose
    var lat: String? = null
    @SerializedName("long")
    @Expose
    var _long: String? = null
    @SerializedName("factor")
    @Expose
    var factor: Any? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("referrer_phone")
    @Expose
    var referrerPhone: Any? = null
    @SerializedName("email")
    @Expose
    var email: Any? = null
    @SerializedName("website")
    @Expose
    var website: Any? = null
    @SerializedName("is_verify")
    @Expose
    var isVerify: Boolean? = null
    @SerializedName("deleted_at")
    @Expose
    var deletedAt: Any? = null
    @SerializedName("logo")
    @Expose
    var logo: Any? = null
    @SerializedName("cover")
    @Expose
    var cover: Any? = null
    @SerializedName("view_count")
    @Expose
    var viewCount: Int? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("is_me")
    @Expose
    var isMe: Boolean? = null
    @SerializedName("icheck_id")
    @Expose
    var icheckId: String? = null
    @SerializedName("distance")
    @Expose
    var distance: Double? = null
}