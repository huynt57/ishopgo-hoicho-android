package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class StampNoDetail : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("quantity")
    @Expose
    var quantity: Int? = null
    @SerializedName("quantity_exists")
    @Expose
    var quantityExists: Int? = null
    @SerializedName("serial_number_prefix")
    @Expose
    var serialNumberPrefix: String? = null
    @SerializedName("serial_number")
    @Expose
    var serialNumber: String? = null
    @SerializedName("assign_product")
    @Expose
    var assignProduct: Int? = null
    @SerializedName("shop_id")
    @Expose
    var shopId: Int? = null
    @SerializedName("calculate_quantity")
    @Expose
    var calculateQuantity: Int? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Int? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}