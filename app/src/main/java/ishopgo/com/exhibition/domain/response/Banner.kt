package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

class Banner {

    @SerializedName("image")
    var image: String? = null
    @SerializedName("action_url")
    var url: String? = null

}
