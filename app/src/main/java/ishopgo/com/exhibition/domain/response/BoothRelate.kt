package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by xuanhong on 5/3/18. HappyCoding!
 */
class BoothRelate {

    @SerializedName("id")
    @Expose
    var id: Long = -1L
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("banner")
    @Expose
    var banner: String? = null
    @SerializedName("hotline")
    @Expose
    var hotline: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null

}