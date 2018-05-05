package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hoangnh on 5/4/2018.
 */
class ProductLike {
    @SerializedName("status")
    @Expose
    var status: Int = 0
}