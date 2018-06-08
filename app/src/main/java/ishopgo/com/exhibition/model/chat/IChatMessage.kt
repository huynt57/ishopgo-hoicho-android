package ishopgo.com.exhibition.model.chat

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
interface IChatMessage {

    fun getMessageId(): Long

    fun getMessageUid(): CharSequence

    fun getCreatedTime(): CharSequence

    fun getConversationId(): CharSequence

    fun getOwnerId(): Long

    fun getOwnerName(): CharSequence

    fun getOwnerAvatar(): CharSequence

    fun getSendStatus(): Int

    fun getMessageType(): Int

    companion object {
        const val STATUS_SENDING = 0
        const val STATUS_SENT = 1
        const val STATUS_FAILED = 2

        const val TYPE_MESSAGE = 0
        const val TYPE_SYSTEM = 1
        const val TYPE_PRODUCT = 3 // my custom
    }
}