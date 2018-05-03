package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.extensions.asMoney
import ishopgo.com.exhibition.ui.main.home.search.product.SearchProductProvider
import ishopgo.com.exhibition.ui.main.product.ProductProvider


/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class Product : IdentityData(), ProductProvider, SearchProductProvider {
    override fun provideCode(): String {
        return code?.trim() ?: ""
    }

    override fun provideImage(): String {
        return image?.trim() ?: ""
    }

    override fun provideName(): String {
        return name?.trim() ?: "unknown"
    }

    override fun providePrice(): String {
        return price.asMoney()
    }

    override fun provideMarketPrice(): String {
        return ttPrice.asMoney()
    }

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("tt_price")
    @Expose
    var ttPrice: Long = 0
    @SerializedName("price")
    @Expose
    var price: Long = 0
    @SerializedName("code")
    @Expose
    var code: String? = null

}