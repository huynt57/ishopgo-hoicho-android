package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class StampSearchProduct {
    @SerializedName("products")
    @Expose
    var products: List<Product>? = null
    @SerializedName("products_total")
    @Expose
    var productsTotal: Int? = null
}