package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class IcheckCountry {
    @SerializedName("id")
    var id: Long = 0
    @SerializedName("alpha_2")
    var alpha2: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("ensign")
    var ensign: String? = null
}