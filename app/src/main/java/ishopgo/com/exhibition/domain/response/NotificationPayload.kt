package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 5/7/18. HappyCoding!
 */
class NotificationPayload {
    @SerializedName("domain")
    val domain: String? = null

    @SerializedName("id")
    val id: Long = 0

    @SerializedName("type")
    val type: String? = null

    @SerializedName("phone")
    val phone: String? = null

    companion object {
        const val TYPE_COMMON = ""
        const val TYPE_PRODUCT = "san_pham"
        const val TYPE_CHAT = "chat"
    }
}