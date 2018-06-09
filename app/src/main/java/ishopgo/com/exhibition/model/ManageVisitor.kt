package ishopgo.com.exhibition.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 6/8/18. HappyCoding!
 */
class ManageVisitor {

    @SerializedName("visit")
    @Expose
    var visitors: List<Visitor>? = null
    @SerializedName("total")
    @Expose
    var total: Long? = null

}