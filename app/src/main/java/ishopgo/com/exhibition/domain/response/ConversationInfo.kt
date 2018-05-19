package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.model.User

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class ConversationInfo {

    @SerializedName("type")
    @Expose
    var type: Int = TYPE_SINGLE

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("listMember")
    @Expose
    var listMember: List<User>? = null

    var conversationId: String? = null

    companion object {
        const val TYPE_SINGLE = 1
        const val TYPE_GROUP = 2
    }
}