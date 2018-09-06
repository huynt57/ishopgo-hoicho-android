package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class StampListWarning {
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("stamp_id")
    @Expose
    var stampId: Long? = null
    @SerializedName("stamp_name")
    @Expose
    var stampName: String? = null
    @SerializedName("stamp_serial_number")
    @Expose
    var stampSerialNumber: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: Long? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("account_phone")
    @Expose
    var accountPhone: String? = null
    @SerializedName("account_name")
    @Expose
    var accountName: String? = null
    @SerializedName("account_email")
    @Expose
    var accountEmail: String? = null
    @SerializedName("product_id")
    @Expose
    var productId: Long? = null
    @SerializedName("product_name")
    @Expose
    var productName: String? = null
    @SerializedName("number_of_scans")
    @Expose
    var numberOfScans: Int? = null
    @SerializedName("number_of_users")
    @Expose
    var numberOfUsers: Int? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("limited_access")
    @Expose
    var limitedAccess: String? = null
    @SerializedName("device_id")
    @Expose
    var deviceId: String? = null
    @SerializedName("status_warning")
    @Expose
    var statusWarning: String? = null
}