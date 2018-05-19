package ishopgo.com.exhibition.domain.response

import com.google.gson.annotations.SerializedName
import ishopgo.com.exhibition.model.chat.ChatImageMessage
import ishopgo.com.exhibition.model.chat.ChatTextMessage
import ishopgo.com.exhibition.model.chat.IChatMessage

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
class PusherChatMessage : ChatTextMessage, ChatImageMessage {
    override fun getText(): CharSequence = apiContent ?: ""

    override fun getMessageId(): Long = -1L

    override fun getMessageUid(): String = uiId ?: ""

    override fun getCreatedTime(): String = apiTime

    override fun getConversationId(): String = idConversation

    override fun getOwnerId(): Long = from

    override fun getOwnerName(): String = name

    override fun getOwnerAvatar(): String = image ?: ""

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

    @SerializedName("type")
    var type: Int = 0

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("lastSender")
    var lastSender: String? = ""
}