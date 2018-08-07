package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.model.Description


class InfoProduct {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("products")
    @Expose
    var products: ProductOfInfo? = null
    @SerializedName("descriptions")
    @Expose
    var descriptions: MutableList<Description>? = null

    class ProductOfInfo : IdentityData() {
        @SerializedName("data")
        @Expose
        var data: MutableList<Product>? = null
    }
}