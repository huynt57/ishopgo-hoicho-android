package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.ui.chat.local.contact.ContactProvider
import ishopgo.com.exhibition.ui.chat.local.group.addmember.IMemberView
import ishopgo.com.exhibition.ui.extensions.asPhone

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class ContactItem : IdentityData(), IMemberView, ContactProvider {
    override fun provideJob(): String {
        return typeText ?: ""
    }

    override fun provideAvatar(): String {
        return image ?: ""
    }

    override fun provideName(): String {
        return name ?: ""
    }

    override fun providePhone(): String {
        return phone?.asPhone() ?: ""
    }

    override fun memberName(): String {
        return name ?: ""
    }

    override fun memberPhone(): String {
        return phone?.asPhone() ?: ""
    }

    override fun memberAvatar(): String {
        return image ?: ""
    }

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