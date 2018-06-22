package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class Ticket : IdentityData() {
    @SerializedName("account_id")
    @Expose
    var accountId: Long = 0L
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("ticket_name")
    @Expose
    var ticketName: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("banner")
    @Expose
    var banner: String? = null
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
}