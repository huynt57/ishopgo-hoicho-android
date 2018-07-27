package ishopgo.com.exhibition.model.product_manager

import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.Product

class ManageProduct {
    @SerializedName("total")
    var total: Int = 0
    @SerializedName("product")
    var product: List<Product>? = null
}