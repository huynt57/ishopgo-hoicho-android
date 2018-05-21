package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.main.configbooth.BoothProvide


/**
 * Created by hoangnh on 5/7/2018.
 */
class Booth : IdentityData(), BoothProvide {
    override fun provideName(): String {
        return name ?: ""
    }

    override fun provideIntroduction(): String {
        return introduction ?: ""
    }

    override fun provideHotline(): String {
        return hotline ?: ""
    }

    override fun provideInfo(): String {
        return info ?: ""
    }

    override fun provideBanner(): String {
        return banner ?: ""
    }

    override fun provideAddress(): String {
        return address ?: ""
    }

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("introduction")
    @Expose
    var introduction: String? = null
    @SerializedName("hotline")
    @Expose
    var hotline: String? = null
    @SerializedName("info")
    @Expose
    var info: String? = null
    @SerializedName("banner")
    @Expose
    var banner: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("qrcode")
    @Expose
    var qrcode: String? = null
}