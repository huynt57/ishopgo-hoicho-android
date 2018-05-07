package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.main.configbooth.BoothProvide


/**
 * Created by hoangnh on 5/7/2018.
 */
class Booth : BoothProvide {
    override fun provideName(): String {
        return name
    }

    override fun provideIntroduction(): String {
        return introduction
    }

    override fun provideHotline(): String {
        return hotline
    }

    override fun provideInfo(): String {
        return info
    }

    override fun provideBanner(): String {
        return banner
    }

    override fun provideAddress(): String {
        return address
    }

    @SerializedName("name")
    @Expose
    var name: String = ""
    @SerializedName("introduction")
    @Expose
    var introduction: String = ""
    @SerializedName("hotline")
    @Expose
    var hotline: String = ""
    @SerializedName("info")
    @Expose
    var info: String = ""
    @SerializedName("banner")
    @Expose
    var banner: String = ""
    @SerializedName("address")
    @Expose
    var address: String = ""
}