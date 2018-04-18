package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
open class IdentityData : BaseDataResponse() {

    @SerializedName("id")
    @Expose
    var id: Long = -1L

}