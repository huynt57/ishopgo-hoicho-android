package ishopgo.com.exhibition.domain.response

import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.main.shop.info.SalePointProvider
import ishopgo.com.exhibition.ui.main.shop.info.ShopInfoProvider


/**
 * Created by xuanhong on 5/2/18. HappyCoding!
 */
class ShopDetail : IdentityData(), ShopInfoProvider {

    override fun provideImage(): String {
        return banner ?: ""
    }

    override fun provideProductCount(): Int {
        return productCount
    }

    override fun provideJoinedDate(): String {
        return if (createdAt.isNullOrBlank()) "01/05/2018" else createdAt!!
    }

    override fun provideRegion(): String {
        return if (address.isNullOrBlank()) "Đang cập nhật" else address!!
    }

    override fun provideRating(): Int {
        return rate
    }

    override fun provideClickCount(): Int {
        return clickCount
    }

    override fun provideShareCount(): Int {
        return shareCount
    }

    override fun provideDescription(): String {
        return if (introduction.isNullOrBlank()) "Đang cập nhật" else introduction!!
    }

    override fun provideSalePoints(): List<SalePointProvider> {
        return mutableListOf()
    }

    override fun provideName(): String {
        return name ?: "Đang cập nhật"
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
    @SerializedName("introduction")
    @Expose
    var introduction: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("clicks")
    @Expose
    var clickCount: Int = 0
    @SerializedName("shares")
    @Expose
    var shareCount: Int = 0
    @SerializedName("info")
    @Expose
    var info: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("product_count")
    @Expose
    var productCount: Int = 0
    @SerializedName("rate")
    @Expose
    var rate: Int = 0
    @SerializedName("qrcode")
    @Expose
    var qrcode: String? = null
    @SerializedName("follow")
    @Expose
    var follow: Boolean = false
}