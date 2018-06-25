package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.main.home.search.shop.SearchShopResultProvider


/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class Shop : IdentityData(), SearchShopResultProvider {
    override fun provideName(): String {
        return name ?: "Tên Gian hàng"
    }

    override fun provideProductCount(): String {
        return numberProduct.toString()
    }

    override fun provideJoinedDate(): String {
        return createdAt?.asDate() ?: ""
    }


    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("banner")
    @Expose
    var banner: String? = null
    @SerializedName("hotline")
    @Expose
    var hotline: String? = null
    @SerializedName("number_product")
    @Expose
    var numberProduct: Int = 0
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

}