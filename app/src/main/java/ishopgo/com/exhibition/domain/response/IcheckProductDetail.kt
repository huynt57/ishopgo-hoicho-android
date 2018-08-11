package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class IcheckProductDetail {
    @SerializedName("id")
    var id: Long = 0
    @SerializedName("content")
    var content: String? = null
}