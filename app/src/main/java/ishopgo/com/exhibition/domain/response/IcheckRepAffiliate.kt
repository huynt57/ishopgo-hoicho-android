package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class IcheckRepAffiliate <T> {
    @SerializedName("statusCode")
    var statusCode: Int = 0
    @SerializedName("data")
    var data: T? = null
}