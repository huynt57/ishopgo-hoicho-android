package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.chat.local.contact.ContactProvider
import ishopgo.com.exhibition.ui.chat.local.group.addmember.IMemberView

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class ContactItem : IMemberView, ContactProvider {
    override fun provideAvatar(): String {
        return image ?: ""
    }

    override fun provideName(): String {
        return name ?: ""
    }

    override fun providePhone(): String {
        return phone ?: ""
    }

    override fun memberName(): String {
        return name ?: ""
    }

    override fun memberPhone(): String {
        return phone ?: ""
    }

    override fun memberAvatar(): String {
        return image ?: ""
    }

    @SerializedName("id")
    @Expose
    var id: Long = 0
    @SerializedName("type_text")
    @Expose
    var typeText: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
}