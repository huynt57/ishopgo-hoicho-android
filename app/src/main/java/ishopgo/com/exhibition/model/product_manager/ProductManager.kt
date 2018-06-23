package ishopgo.com.exhibition.model.product_manager

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.extensions.asMoney

class ProductManager : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("tt_price")
    @Expose
    var ttPrice: Long? = null
    @SerializedName("price")
    @Expose
    var price: Long? = null
    @SerializedName("status")
    var status: Int? = null
    @SerializedName("department")
    var department: String? = null
    @SerializedName("created_at")
    var provider_price: Long = 0
    @SerializedName("provider_code")
    var provider_code: Int = 0
    @SerializedName("barcode")
    var barcode: String? = null
    @SerializedName("category")
    var category: String? = null
    @SerializedName("total")
    var total: Int = 0
}