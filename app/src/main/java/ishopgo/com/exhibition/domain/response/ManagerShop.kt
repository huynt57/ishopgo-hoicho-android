package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class ManagerShop {
    @SerializedName("total")
    var total: Int = 0
    @SerializedName("booth")
    var booth: List<Shop>? = null
}