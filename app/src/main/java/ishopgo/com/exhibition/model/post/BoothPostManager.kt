package ishopgo.com.exhibition.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class BoothPostManager {
    @SerializedName("total_post")
    @Expose
    var totalPost: Int? = null
    @SerializedName("posts")
    @Expose
    var posts: MutableList<PostObject>? = null
}