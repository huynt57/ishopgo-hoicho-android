package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StampUserListScan : IdentityData() {
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("stamp_id")
    @Expose
    var stampId: Int? = null
    @SerializedName("stamp_name")
    @Expose
    var stampName: Any? = null
    @SerializedName("stamp_serial_number")
    @Expose
    var stampSerialNumber: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: Int? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("account_phone")
    @Expose
    var accountPhone: String? = null
    @SerializedName("account_name")
    @Expose
    var accountName: String? = null
    @SerializedName("product_id")
    @Expose
    var productId: Int? = null
    @SerializedName("product_name")
    @Expose
    var productName: String? = null
    @SerializedName("number_of_scans")
    @Expose
    var numberOfScans: Int? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("limited_access")
    @Expose
    var limitedAccess: String? = null
    @SerializedName("detail")
    @Expose
    var detail: List<StampUserListScanDetail>? = null
}