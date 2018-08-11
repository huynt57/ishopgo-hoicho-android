package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IcheckMeta {
    @SerializedName("total")
    @Expose
    var total: Int = 0
}