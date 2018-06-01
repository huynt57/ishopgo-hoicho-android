package ishopgo.com.exhibition.model.search_sale_point

import com.google.gson.annotations.SerializedName

class ManagerSalePointDetail  {
    @SerializedName("salePoint")
    var salePoint: SearchSalePoint? = null
    @SerializedName("products")
    var products: ProductSalePoint? = null
}