package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName

/**
 * Created by xuanhong on 5/7/18. HappyCoding!
 */
class NotificationPayload {
    @field:SerializedName("domain")
    val domain: String? = null

    @field:SerializedName("id")
    val id: Long = 0

    @field:SerializedName("type")
    val type: String? = null

    @field:SerializedName("phone")
    val phone: String? = null

    companion object {
        const val TYPE_PRODUCT = "san_pham"
    }
}