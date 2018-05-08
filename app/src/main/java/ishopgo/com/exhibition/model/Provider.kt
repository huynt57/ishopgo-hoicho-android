package ishopgo.com.exhibition.model

import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData

class Provider : IdentityData() {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("company")
    var company: String? = null
    @SerializedName("phone")
    var phone: String? = null
    @SerializedName("address")
    var address: String? = null
    @SerializedName("number_product")
    var numberProduct: Int = 0
    @SerializedName("region")
    var region: String? = null
    @SerializedName("sku")
    var sku: Int = 0
}