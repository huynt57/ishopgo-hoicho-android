package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.model.chat.ChatImageMessage
import ishopgo.com.exhibition.model.chat.ChatProductMessage
import ishopgo.com.exhibition.model.chat.ChatTextMessage
import ishopgo.com.exhibition.model.chat.IChatMessage
import ishopgo.com.exhibition.ui.extensions.asHtml

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class LocalMessageItem : ChatTextMessage, ChatImageMessage, ChatProductMessage {

    override fun getEmbededProduct(): Product? {
        return product
    }

    override fun getMessageType(): Int {
        return if (product != null) IChatMessage.TYPE_PRODUCT
        else
            when (type) {
                1 -> IChatMessage.TYPE_SYSTEM
                else -> IChatMessage.TYPE_MESSAGE
            }
    }

    override fun getText(): CharSequence = content.asHtml()

    override fun getMessageId(): Long = id

    override fun getMessageUid(): CharSequence = "$id"

    override fun getCreatedTime(): CharSequence = time

    override fun getConversationId(): CharSequence = idConversation

    override fun getOwnerId(): Long = from

    override fun getOwnerName(): CharSequence = accountName

    override fun getOwnerAvatar(): CharSequence = accountAvatar

    override fun getSendStatus(): Int = IChatMessage.STATUS_SENT

    override fun getImageUrls(): List<String> = images ?: listOf()

    @SerializedName("id")
    @Expose
    var id: Long = 0
    @SerializedName("id_conversion")
    @Expose
    var idConversation: String = ""
    @SerializedName("content")
    @Expose
    var content: String = ""
    @SerializedName("from")
    @Expose
    var from: Long = 0
    @SerializedName("images")
    @Expose
    var images: MutableList<String>? = null
    @SerializedName("account_name")
    @Expose
    var accountName: String = ""
    @SerializedName("account_avatar")
    @Expose
    var accountAvatar: String = ""
    @SerializedName("created_at")
    @Expose
    var time: String = ""
    @SerializedName("type")
    var type: Int = 0
    @SerializedName("product")
    var product: Product? = null
}