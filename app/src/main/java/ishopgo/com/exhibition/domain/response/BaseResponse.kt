package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
open class BaseResponse<T> {

    @SerializedName("status")
    @Expose
    var status: Int = 0

    @SerializedName("data")
    @Expose
    var data: T? = null

    @SerializedName("message")
    @Expose
    var message: String? = null


}