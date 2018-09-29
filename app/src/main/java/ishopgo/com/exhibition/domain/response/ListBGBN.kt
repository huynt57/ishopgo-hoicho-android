package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ListBGBN : IdentityData() {
    @SerializedName("booth_name")
    @Expose
    var boothName: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("type")
    @Expose
    var type: Int? = null
}