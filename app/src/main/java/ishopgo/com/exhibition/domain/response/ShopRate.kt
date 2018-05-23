package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.model.User
import ishopgo.com.exhibition.ui.extensions.asDate
import ishopgo.com.exhibition.ui.main.shop.rate.ShopRateProvider


/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class ShopRate : IdentityData(), ShopRateProvider {
    override fun provideRating(): Float {
        return ratePoint?.toFloat() ?: 0.0F
    }

    override fun provideName(): String {
        val name = account?.name
        return if (name.isNullOrBlank()) "Người dùng ẩn danh" else name!!
    }

    override fun provideAvatar(): String {
        return account?.image ?: ""
    }

    override fun provideTime(): String {
        return time?.asDate() ?: ""
    }

    override fun provideContent(): String {
        return content ?: ""
    }

    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("account")
    @Expose
    var account: User? = null
    @SerializedName("time")
    @Expose
    var time: String? = null
    @SerializedName("rate_point")
    @Expose
    var ratePoint: Int? = null

}