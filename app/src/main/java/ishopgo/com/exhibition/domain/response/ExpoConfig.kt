package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by xuanhong on 6/18/18. HappyCoding!
 */
class ExpoConfig {

    @SerializedName("id")
    @Expose
    var id: Long? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("start_time")
    @Expose
    var startTime: String? = null
    @SerializedName("end_time")
    @Expose
    var endTime: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("qrcode")
    @Expose
    var qrcode: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("map")
    @Expose
    var map: String? = null
    @SerializedName("count")
    @Expose
    var count: Int? = null
}