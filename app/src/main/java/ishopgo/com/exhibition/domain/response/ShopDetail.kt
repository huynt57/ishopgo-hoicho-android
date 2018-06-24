package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class ShopDetail : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("banner")
    @Expose
    var banner: String? = null
    @SerializedName("hotline")
    @Expose
    var hotline: String? = null
    @SerializedName("introduction")
    @Expose
    var introduction: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("clicks")
    @Expose
    var clickCount: Int = 0
    @SerializedName("shares")
    @Expose
    var shareCount: Int = 0
    @SerializedName("info")
    @Expose
    var info: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("product_count")
    @Expose
    var productCount: Int = 0
    @SerializedName("rate")
    @Expose
    var rate: Int = 0
    @SerializedName("qrcode")
    @Expose
    var qrcode: String? = null
    @SerializedName("follow")
    @Expose
    var follow: Boolean = false
    @SerializedName("count_follow")
    @Expose
    var followCount: Int? = null
    @SerializedName("visit")
    @Expose
    var visitCount: Int? = null
}