package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.ExpoConfig
import ishopgo.com.exhibition.domain.response.IdentityData


class Ticket : IdentityData() {
    @SerializedName("account_id")
    @Expose
    var accountId: Long = 0L
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("district")
    @Expose
    var district: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("fair")
    @Expose
    var fair: ExpoConfig? = null
    @SerializedName("avatar")
    @Expose
    var avatar: String? = null
    @SerializedName("payment_status")
    @Expose
    var paymentStatus: Int? = null
    @SerializedName("payment_status_text")
    @Expose
    var paymentStatusText: String? = null
}