package ishopgo.com.exhibition.model

import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData

class Brand : IdentityData() {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("logo")
    var logo: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("address")
    var address: String? = null
    @SerializedName("account_id")
    var accountId: String? = null
    @SerializedName("created_at")
    var createdAt: String? = null
    @SerializedName("updated_at")
    var updatedAt: String? = null
    @SerializedName("is_featured")
    var isFeatured: Int = 0
    @SerializedName("shop_id")
    var shopId: Int = 0
}