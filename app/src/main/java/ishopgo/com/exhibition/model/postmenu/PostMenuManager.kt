package ishopgo.com.exhibition.model.postmenu

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.model.post.PostObject


class PostMenuManager {
    @SerializedName("total_post")
    @Expose
    var totalPost: Int? = null
    @SerializedName("posts")
    @Expose
    var posts: List<PostObject>? = null
}