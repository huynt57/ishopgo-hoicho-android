package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.model.District
import ishopgo.com.exhibition.model.Region


class IcheckSalePoint : IdentityData() {
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
    @SerializedName("email")
    @Expose
    var email: Any? = null
    @SerializedName("website")
    @Expose
    var website: Any? = null
    @SerializedName("is_verify")
    @Expose
    var isVerify: Boolean? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("city")
    @Expose
    var city: IcheckCountry? = null
    @SerializedName("district")
    @Expose
    var district: IcheckCountry? = null
    @SerializedName("is_me")
    @Expose
    var isMe: Boolean? = null
    @SerializedName("distance")
    @Expose
    var distance: Double? = null
    @SerializedName("price")
    @Expose
    var price: Long? = null
}