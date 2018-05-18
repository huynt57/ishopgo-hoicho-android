package ishopgo.com.exhibition.model.chat

/**
 * Created by xuanhong on 5/17/18. HappyCoding!
 */
interface ChatImageMessage : IChatMessage {

    fun getImageUrls(): List<String>
}