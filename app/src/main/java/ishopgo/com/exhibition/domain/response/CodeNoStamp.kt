package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CodeNoStamp {
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("total")
    @Expose
    var total: Int? = null
}