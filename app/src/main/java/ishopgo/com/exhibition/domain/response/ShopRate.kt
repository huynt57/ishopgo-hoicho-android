package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.model.User


/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class ShopRate : IdentityData() {
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("account")
    @Expose
    var account: User? = null
    @SerializedName("time")
    @Expose
    var time: String? = null
    @SerializedName("rate_point")
    @Expose
    var ratePoint: Int? = null

}