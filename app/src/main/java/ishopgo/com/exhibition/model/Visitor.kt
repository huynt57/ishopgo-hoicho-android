package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData

/**
 * Created by xuanhong on 6/8/18. HappyCoding!
 */
class Visitor : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Long? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
}