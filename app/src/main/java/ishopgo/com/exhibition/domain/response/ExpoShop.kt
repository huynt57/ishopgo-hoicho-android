package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by xuanhong on 6/18/18. HappyCoding!
 */
class ExpoShop {

    @SerializedName("id")
    @Expose
    var id: Long? = null
    @SerializedName("priority")
    @Expose
    var priority: Int? = null
    @SerializedName("booth_id")
    @Expose
    var boothId: Long? = null
    @SerializedName("booth")
    @Expose
    var booth: Booth? = null

    inner class Booth {

        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("phone")
        @Expose
        var phone: String? = null
        @SerializedName("city")
        @Expose
        var city: String? = null

    }
}