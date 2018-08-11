package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class IcheckRep <T> {
    @SerializedName("status")
    var status: Int = 0
    @SerializedName("data")
    var data: T? = null
}