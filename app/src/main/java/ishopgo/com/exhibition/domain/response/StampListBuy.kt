package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class StampListBuy : IdentityData() {
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("quantity")
    @Expose
    var quantity: Int = 0
    @SerializedName("user_id")
    @Expose
    var userId: Int? = null
    @SerializedName("user_name")
    @Expose
    var userName: String? = null
    @SerializedName("user_email")
    @Expose
    var userEmail: String? = null
    @SerializedName("user_phone")
    @Expose
    var userPhone: String? = null
    @SerializedName("note")
    @Expose
    var note: String? = null
    @SerializedName("unit_price")
    @Expose
    var unitPrice: Long? = null
    @SerializedName("price_total")
    @Expose
    var priceTotal: Long? = null
    @SerializedName("status_id")
    @Expose
    var statusId: Long? = null
    @SerializedName("status_name")
    @Expose
    var statusName: String? = null
}