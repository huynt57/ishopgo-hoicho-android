package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.model.search_sale_point.SearchSalePoint


class ManagerShopDetail {
    @SerializedName("booth")
    @Expose
    var booth: ShopDetail? = null
    @SerializedName("salePoint")
    @Expose
    var salePoint: List<SearchSalePoint>? = null
}