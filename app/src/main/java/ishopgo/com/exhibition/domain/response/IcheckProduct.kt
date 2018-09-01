package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class IcheckProduct : IdentityData() {
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
    @SerializedName("like_count")
    var likeCount: Int? = null
    @SerializedName("review_count")
    var reviewCount: Int? = null
    @SerializedName("comment_count")
    var commentCount: Int? = null
    @SerializedName("seller_count")
    var sellerCount: Int? = null
    @SerializedName("star")
    var star: Float? = null
}