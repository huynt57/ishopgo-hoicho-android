package ishopgo.com.exhibition.domain.request

/**
 * Created by xuanhong on 4/18/18. HappyCoding!
 */
open class LoadMoreCommunityRequest : Request() {

    var limit: Int = 0

    var last_id: Long = 0

}