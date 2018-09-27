package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class StampNoListNew : IdentityData() {
    @SerializedName("code")
    @Expose
    var code: String? = null
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
    @SerializedName("quantity")
    @Expose
    var quantity: Int? = null
    @SerializedName("download")
    @Expose
    var download: String? = null
    @SerializedName("start_quantity")
    @Expose
    var startQuantity: Int? = null
    @SerializedName("end_quantity")
    @Expose
    var endQuantity: Int? = null
    @SerializedName("note")
    @Expose
    var note: String? = null
    @SerializedName("limited_access")
    @Expose
    var limitedAccess: Int? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}