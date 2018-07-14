package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class Booth : IdentityData() {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("intro")
    @Expose
    var intro: String? = null
    @SerializedName("info")
    @Expose
    var info: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("hotline")
    @Expose
    var hotline: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("district")
    @Expose
    var district: String? = null
    @SerializedName("count")
    @Expose
    var count: Int? = null
    @SerializedName("followed")
    @Expose
    var followed: Int = 0
    @SerializedName("rate")
    @Expose
    var rate: Int? = null
    @SerializedName("rate_count")
    @Expose
    var rateCount: Int? = null

    companion object {
        val FOllWED = 1
    }

    fun isFollowed() = followed == FOllWED

}