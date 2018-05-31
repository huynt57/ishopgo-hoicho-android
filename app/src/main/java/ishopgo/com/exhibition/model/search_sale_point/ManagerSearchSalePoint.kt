package ishopgo.com.exhibition.model.search_sale_point

import com.google.gson.annotations.SerializedName

class ManagerSearchSalePoint {
    @SerializedName("total")
    var total: Int = 0
    @SerializedName("salePoint")
    var salePoint: MutableList<SearchSalePoint>? = null
}