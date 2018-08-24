package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class StampUserListScanDetail : IdentityData() {
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
    @SerializedName("product_id")
    @Expose
    var productId: Int? = null
    @SerializedName("product_name")
    @Expose
    var productName: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("note")
    @Expose
    var note: String? = null
    @SerializedName("device_id")
    @Expose
    var deviceId: Any? = null
    @SerializedName("mac_address")
    @Expose
    var macAddress: Any? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("count_scan")
    @Expose
    var countScan: Int? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
}