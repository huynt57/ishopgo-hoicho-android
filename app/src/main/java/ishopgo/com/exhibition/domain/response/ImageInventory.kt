package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class ImageInventory {
    @SerializedName("id")
    @Expose
    var id: Long = 0

    @SerializedName("link")
    @Expose
    var link: String? = null
}