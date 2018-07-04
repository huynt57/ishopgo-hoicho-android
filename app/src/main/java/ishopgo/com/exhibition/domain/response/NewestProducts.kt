package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 6/25/18. HappyCoding!
 */
class NewestProducts {

    @SerializedName("total")
    @Expose
    var total: Long? = null
    @SerializedName("product")
    @Expose
    var data: List<Product>? = null

}