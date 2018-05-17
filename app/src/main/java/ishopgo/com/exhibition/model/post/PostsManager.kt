package ishopgo.com.exhibition.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class PostsManager {
    @SerializedName("total_post")
    @Expose
    var totalPost: Int? = null
    @SerializedName("subcribe_isg")
    @Expose
    var subcribeIsg: Int? = null
    @SerializedName("objects")
    @Expose
    var objects: List<PostObject>? = null
}