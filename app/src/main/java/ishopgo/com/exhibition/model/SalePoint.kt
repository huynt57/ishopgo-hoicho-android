package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData

class SalePoint : IdentityData() {
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
    @SerializedName("price")
    @Expose
    var price: Long? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("product_name")
    @Expose
    var productName: String? = null
}