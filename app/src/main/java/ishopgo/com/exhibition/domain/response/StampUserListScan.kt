package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StampUserListScan : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("create_at")
    @Expose
    var createAt: Long? = null
    @SerializedName("scan_total")
    @Expose
    var scanTotal: Int? = null
}