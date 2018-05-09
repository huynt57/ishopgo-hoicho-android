package ishopgo.com.exhibition.model.product_manager

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hoangnh on 3/20/2018.
 */
class ProductRelated {
    @SerializedName("products")
    @Expose
    val products: List<Product>? = null

    class Product {
        @SerializedName("id")
        @Expose
        val id: Long? = null

        @SerializedName("name")
        @Expose
        val name: String? = null

        @SerializedName("price")
        @Expose
        val price: Long? = null

        @SerializedName("image")
        @Expose
        val image: String? = null
    }
}