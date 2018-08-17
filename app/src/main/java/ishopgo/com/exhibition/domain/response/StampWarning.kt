package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StampWarning : IdentityData() {
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("create_at")
    @Expose
    var createAt: String? = null
    @SerializedName("product_name")
    @Expose
    var productName: String? = null
    @SerializedName("stamp_id")
    @Expose
    var stampId: Long? = null
    @SerializedName("product_id")
    @Expose
    var productId: Long? = null
    @SerializedName("assign_id")
    @Expose
    var assignId: Long? = null
    @SerializedName("scan_total")
    @Expose
    var scanTotal: Int? = null
    @SerializedName("scan_user_total")
    @Expose
    var scanUserTotal: Int? = null
    @SerializedName("note")
    @Expose
    var note: String? = null
}