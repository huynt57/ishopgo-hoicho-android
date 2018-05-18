package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class LocalConversationItem {

    @SerializedName("id")
    @Expose
    var id: Long = 0
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("id_conversions")
    @Expose
    var idConversions: String = ""
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("time")
    @Expose
    var time: String? = null
    @SerializedName("last_msg_time")
    @Expose
    var lastMsgTime: String? = null
    @SerializedName("read")
    @Expose
    var unreadCount: Int = 0

}