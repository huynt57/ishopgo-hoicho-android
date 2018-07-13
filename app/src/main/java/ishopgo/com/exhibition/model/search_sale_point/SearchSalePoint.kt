package ishopgo.com.exhibition.model.search_sale_point

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData


class SearchSalePoint : IdentityData() {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("district")
    @Expose
    var district: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("count_product")
    @Expose
    var countProduct: Int? = null
    @SerializedName("account_id")
    @Expose
    var accountId: Long? = 0
    @SerializedName("chat_id")
    @Expose
    var chatId: Long? = 0
    @SerializedName("qrcode")
    @Expose
    var qrcode: String? = null
}