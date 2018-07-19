package ishopgo.com.exhibition.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostContent {
    @SerializedName("content")
    @Expose
    val content: String? = null
    @SerializedName("link_share")
    @Expose
    val linkShare: String? = null
}