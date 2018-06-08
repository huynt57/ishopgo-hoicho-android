package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.model.chat.ChatImageMessage
import ishopgo.com.exhibition.model.chat.ChatProductMessage
import ishopgo.com.exhibition.model.chat.ChatTextMessage
import ishopgo.com.exhibition.model.chat.IChatMessage
import ishopgo.com.exhibition.ui.extensions.asHtml

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class PusherChatMessage : ChatTextMessage, ChatImageMessage, ChatProductMessage {

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

    override fun getText(): CharSequence = apiContent?.asHtml() ?: ""

    override fun getMessageId(): Long = -1L

    override fun getMessageUid(): CharSequence = uiId ?: ""

    override fun getCreatedTime(): CharSequence = apiTime

    override fun getConversationId(): CharSequence = idConversation

    override fun getOwnerId(): Long = from

    override fun getOwnerName(): CharSequence = name

    override fun getOwnerAvatar(): CharSequence = image ?: ""

    override fun getSendStatus(): Int = IChatMessage.STATUS_SENT

    override fun getImageUrls(): List<String> = images ?: listOf()

    @SerializedName("idConversion")
    var idConversation: String = ""

    @SerializedName("api_content")
    var apiContent: String? = ""

    @SerializedName("previewContent")
    var previewContent: String? = ""

    @SerializedName("api_time")
    var apiTime: String = ""

    @SerializedName("image")
    var image: String? = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("from")
    var from: Long = 0

    @SerializedName("images")
    var images: MutableList<String>? = null

    @SerializedName("ui_id")
    var uiId: String? = ""

    @SerializedName("conversation_name")
    var conversationName: String? = null

    // type = 1 la system message
    @SerializedName("type")
    var type: Int = 0

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("lastSender")
    var lastSender: String? = ""

    @SerializedName("product")
    var product: Product? = null
}