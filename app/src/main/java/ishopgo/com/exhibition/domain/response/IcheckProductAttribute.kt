package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class IcheckProductAttribute {
    @SerializedName("id")
    var id: Long = 0
    @SerializedName("title")
    var title: String? = null
    @SerializedName("icon")
    var icon: String? = null
    @SerializedName("short_content")
    var shortContent: String? = null
}