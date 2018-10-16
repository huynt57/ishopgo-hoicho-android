package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ValueSync : IdentityData() {
    @SerializedName("type")
    @Expose
    var type: Int? = null
    @SerializedName("sale_point_id")
    @Expose
    var salePointId: Int? = null
    @SerializedName("relate_booth_id")
    @Expose
    var relateBoothId: Any? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Int? = null
    @SerializedName("shop_id")
    @Expose
    var shopId: Int? = null
    @SerializedName("product_id")
    @Expose
    var productId: Int? = null
    @SerializedName("booth_id")
    @Expose
    var boothId: Int? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("district")
    @Expose
    var district: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("from_id")
    @Expose
    var fromId: Any? = null
    @SerializedName("to_id")
    @Expose
    var toId: Any? = null
    @SerializedName("content")
    @Expose
    var content: Any? = null
    @SerializedName("lat")
    @Expose
    var lat: String? = null
    @SerializedName("lng")
    @Expose
    var lng: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: Any? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: Any? = null
}