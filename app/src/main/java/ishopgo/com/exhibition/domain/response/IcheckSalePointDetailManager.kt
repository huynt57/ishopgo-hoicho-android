package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IcheckSalePointDetailManager {
    @SerializedName("meta")
    @Expose
    var meta: IcheckMeta? = null
    @SerializedName("local")
    @Expose
    var local: IcheckSalePointDetail? = null
}