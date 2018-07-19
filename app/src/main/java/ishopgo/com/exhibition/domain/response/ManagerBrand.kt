package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class ManagerBrand {
    @SerializedName("total")
    var total: Int = 0
    @SerializedName("brands")
    var brand: List<Brand>? = null
}