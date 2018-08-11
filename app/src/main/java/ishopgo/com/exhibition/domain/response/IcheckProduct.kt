package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class IcheckProduct {
    @SerializedName("gtin_code")
    var code: String? = null
    @SerializedName("product_name")
    var productName: String? = null
    @SerializedName("image_default")
    var imageDefault: String? = null
    @SerializedName("price_default")
    var priceDefault: Long = 0
    @SerializedName("vendor")
    var vendor: IcheckVendor? = null
    @SerializedName("attributes")
    var attributes: List<IcheckProductAttribute>? = null
    @SerializedName("isClone")
    var isClone: Boolean? = null
}