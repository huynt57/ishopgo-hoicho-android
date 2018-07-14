package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class InfoProduct {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("products")
    @Expose
    var products: ProductOfInfo? = null

    class ProductOfInfo : IdentityData() {
        @SerializedName("data")
        @Expose
        var data: List<Product>? = null
    }
}