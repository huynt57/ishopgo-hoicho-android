package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IcheckProductManager {
    @SerializedName("meta")
    @Expose
    var meta: IcheckMeta? = null
    @SerializedName("list")
    @Expose
    var list: List<IcheckProduct>? = null
}