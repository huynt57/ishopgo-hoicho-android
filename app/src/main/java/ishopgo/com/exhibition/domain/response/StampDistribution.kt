package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StampDistribution {
    @SerializedName("stamp_id")
    @Expose
    var stampId: Int? = null
    @SerializedName("stamp_name")
    @Expose
    var stampName: Any? = null
    @SerializedName("stamp_serial_number")
    @Expose
    var stampSerialNumber: String? = null
    @SerializedName("stamp_serial_number_prefix")
    @Expose
    var stampSerialNumberPrefix: String? = null
    @SerializedName("shop_id")
    @Expose
    var shopId: Int? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Int? = null
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
    @SerializedName("parent_id")
    @Expose
    var parentId: Int? = null
    @SerializedName("quantity")
    @Expose
    var quantity: Int? = null
    @SerializedName("start_quantity")
    @Expose
    var startQuantity: Int? = null
    @SerializedName("end_quantity")
    @Expose
    var endQuantity: Int? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("limited_access")
    @Expose
    var limitedAccess: Int? = null
    @SerializedName("limited_access_message")
    @Expose
    var limitedAccessMessage: String? = null
}