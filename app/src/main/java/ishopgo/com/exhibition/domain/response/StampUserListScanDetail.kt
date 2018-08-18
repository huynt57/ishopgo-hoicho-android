package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class StampUserListScanDetail : IdentityData() {
    @SerializedName("stamp_id")
    @Expose
    var stampId: Int? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("user_scan_id")
    @Expose
    var userScanId: Int? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("note")
    @Expose
    var note: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("product_id")
    @Expose
    var productId: Int? = null
}