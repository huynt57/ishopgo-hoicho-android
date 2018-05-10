package ishopgo.com.exhibition.model.product_manager

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.Product

/**
 * Created by hoangnh on 3/20/2018.
 */
class ProductRelated {
    @SerializedName("products")
    @Expose
    val products: List<Product>? = null
}