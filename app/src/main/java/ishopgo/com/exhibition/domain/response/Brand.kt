package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.main.brand.HighlightBrandProvider

/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class Brand : IdentityData(), HighlightBrandProvider {
    override fun provideImage(): String {
        return logo ?: ""
    }

    @SerializedName("logo")
    var logo: String? = null
    @SerializedName("name")
    var name: String? = null
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