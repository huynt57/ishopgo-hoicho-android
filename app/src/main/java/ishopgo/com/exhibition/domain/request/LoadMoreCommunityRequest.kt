package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
open class LoadMoreCommunityRequest : Request() {
    var post_id: Long = 0

    var limit: Int = 0

    var last_id: Long = 0

    var parent_id: Long = 0

}