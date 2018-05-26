package ishopgo.com.exhibition.model.community

import com.google.gson.annotations.SerializedName

class ManagerCommunity {
    @SerializedName("total")
    var total: Int = 0
    @SerializedName("post")
    var post: List<Community>? = null
}