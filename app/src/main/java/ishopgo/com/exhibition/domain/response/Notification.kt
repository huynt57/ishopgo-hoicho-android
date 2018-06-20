package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 5/7/18. HappyCoding!
 */
class Notification {
    @SerializedName("id")
    var id: Long = -1L
    @SerializedName("account")
    var account: String? = null
    @SerializedName("title")
    var title: String? = null
    @SerializedName("image")
    var image: String? = null
    @SerializedName("content")
    var content: String? = null
    @SerializedName("short_description")
    var shortDescription: String? = null
    @SerializedName("created_at")
    var createdAt: String? = null
    @SerializedName("sender")
    var sender: String? = null
    @SerializedName("account_image")
    var accountImage: String? = null
    @SerializedName("is_read")
    var isRead: Int = 0
    @SerializedName("payload_data")
    val payloadData: NotificationPayload? = null

}