package ishopgo.com.exhibition.model

import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.domain.response.IdentityData
import ishopgo.com.exhibition.ui.main.notification.NotificationProvider

/**
 * Created by hoangnh on 5/7/2018.
 */
class Notification : IdentityData(), NotificationProvider {
    override fun provideId(): Long {
        return id
    }

    override fun provideAccount(): String {
        return account
    }

    override fun provideTitle(): String {
        return title
    }

    override fun provideImage(): String {
        return image
    }

    override fun provideContent(): String {
        return content
    }

    override fun provideShortDescription(): String {
        return shortDescription
    }

    override fun provideCreatedAt(): String {
        return createdAt
    }

    override fun provideSender(): String {
        return sender
    }

    override fun provideIsRead(): Int {
        return isRead
    }

    @SerializedName("account")
    var account: String = ""
    @SerializedName("title")
    var title: String = ""
    @SerializedName("image")
    var image: String = ""
    @SerializedName("content")
    var content: String = ""
    @SerializedName("short_description")
    var shortDescription: String = ""
    @SerializedName("created_at")
    var createdAt: String = ""
    @SerializedName("sender")
    var sender: String = ""
    @SerializedName("is_read")
    var isRead: Int = 0
}