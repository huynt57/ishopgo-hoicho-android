package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class ManagerProduct {
    @SerializedName("total")
    var total: Int = 0
    @SerializedName("product")
    var product: List<Product>? = null
}