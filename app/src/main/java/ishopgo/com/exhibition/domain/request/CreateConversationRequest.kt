package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 5/29/18. HappyCoding!
 */
class CreateConversationRequest: Request() {

    var type: Int = 1
    var member: List<Long> = listOf()

    companion object {
        const val TYPE_SINGLE = 1
        const val TYPE_GROUP = 2
    }
}