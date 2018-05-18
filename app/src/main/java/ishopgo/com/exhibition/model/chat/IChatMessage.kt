package ishopgo.com.exhibition.model.chat

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
interface IChatMessage {

    fun getMessageId(): Long

    fun getMessageUid(): String

    fun getCreatedTime(): String

    fun getConversationId(): String

    fun getOwnerId(): Long

    fun getOwnerName(): String

    fun getOwnerAvatar(): String

    fun getSendStatus(): Int

    companion object {
        const val STATUS_SENDING = 0
        const val STATUS_SENT = 1
        const val STATUS_FAILED = 2

        const val TYPE_MESSAGE = 0
        const val TYPE_SYSTEM = 1
    }
}