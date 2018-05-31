package ishopgo.com.exhibition.model.search_sale_point

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.Product


class ProductSalePoint {
    @SerializedName("data")
    @Expose
    var data: MutableList<Product>? = null
}