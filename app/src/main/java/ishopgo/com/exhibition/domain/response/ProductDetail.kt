package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class ProductDetail : IdentityData() {
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("price")
    @Expose
    var price: Long = 0
    @SerializedName("promotion_price")
    @Expose
    var promotionPrice: Long = 0
    @SerializedName("tt_price")
    @Expose
    var ttPrice: Long = 0
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("shares")
    @Expose
    var shares: Int = 0
    @SerializedName("likes")
    @Expose
    var likes: Int = 0
    @SerializedName("liked")
    @Expose
    val liked: Int = 0
    @SerializedName("followed")
    @Expose
    var followed: Int = 0
    @SerializedName("comments")
    @Expose
    var comments: Int = 0
    @SerializedName("department")
    @Expose
    var department: Department? = null
    @SerializedName("booth")
    @Expose
    var booth: Booth? = null
    @SerializedName("link_affiliate")
    @Expose
    var linkAffiliate: String? = null
    @SerializedName("images")
    @Expose
    var images: MutableList<String>? = null
    @SerializedName("wholesale_price_from")
    @Expose
    var wholesalePriceFrom: Long = 0
    @SerializedName("wholesale_price_to")
    @Expose
    var wholesalePriceTo: Long = 0
    @SerializedName("wholesale_count_product")
    @Expose
    var wholesaleCountProduct: Int = 0
    @SerializedName("view_wholesale")
    @Expose
    var viewWholesale: Int? = null
}