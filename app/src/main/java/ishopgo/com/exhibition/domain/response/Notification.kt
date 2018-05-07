package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.main.notification.NotificationProvider

/**
 * Created by xuanhong on 5/7/18. HappyCoding!
 */
class Notification : IdentityData(), NotificationProvider {
    override fun provideWasRed(): Boolean {
        return wasRead()
    }

    override fun provideId(): Long {
        return id
    }

    override fun provideAccount(): String {
        return account ?: ""
    }

    override fun provideTitle(): String {
        return title ?: ""
    }

    override fun provideImage(): String {
        return image ?: ""
    }

    override fun provideContent(): String {
        return content ?: ""
    }

    override fun provideShortDescription(): String {
        return shortDescription ?: ""
    }

    override fun provideCreatedAt(): String {
        return createdAt ?: ""
    }

    override fun provideSender(): String {
        return sender ?: ""
    }

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
    @SerializedName("is_read")
    var isRead: Int = 0
    @SerializedName("payload_data")
    val payloadData: NotificationPayload? = null


    companion object {
        val TYPE_PERSONAL = 1
    }

    fun wasRead() = isRead == TYPE_PERSONAL
}